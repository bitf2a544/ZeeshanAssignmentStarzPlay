package com.example.zeeshanassignment.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zeeshanassignment.R
import com.example.zeeshanassignment.adapter.CarouselListAdapter
import com.example.zeeshanassignment.data.model.CarouselItem
import com.example.zeeshanassignment.data.model.Carousels
import com.example.zeeshanassignment.databinding.CardsListFragmentBinding
import com.example.zeeshanassignment.enum.MediaTypes
import com.example.zeeshanassignment.ui.CarouselItemClickListener
import com.example.zeeshanassignment.utils.Constants.Companion.CARD_PARAM
import com.example.zeeshanassignment.utils.Status
import com.example.zeeshanassignment.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.Charset


@AndroidEntryPoint
class HomeListingFragment : Fragment(), CarouselItemClickListener {
    private lateinit var binding: CardsListFragmentBinding
    private lateinit var cardsAdapter: CarouselListAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CardsListFragmentBinding.inflate(layoutInflater)
        setupUI()
        fetchCardsListing()
        setupObserver()
        setUpSearchField()
        return binding.root
    }

    private fun setupUI() {
        setUpCurrencyRecyclerView()
    }

    private fun setUpCurrencyRecyclerView() {
        binding.currencyRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        cardsAdapter = CarouselListAdapter(requireContext(), arrayListOf(), this)
        binding.currencyRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.currencyRecyclerView.context,
                (binding.currencyRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.currencyRecyclerView.adapter = cardsAdapter
    }

    private fun setUpSearchField() {
        binding.searchET.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(!binding.searchET.text.toString().isNullOrEmpty()) {
                    Toast.makeText(requireContext(), binding.searchET.getText(), Toast.LENGTH_SHORT)
                        .show()
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.searchET.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if(!binding.searchET.text.toString().isNullOrEmpty()) {
                    val drawableEnd =
                        binding.searchET.compoundDrawablesRelative[2] // index 2 is for drawableEnd
                    if (drawableEnd != null) {
                        val drawableWidth = drawableEnd.bounds.width()
                        val touchX = event.x
                        val viewWidth = binding.searchET.width

                        // Check if the touch is within the drawableEnd bounds
                        if (touchX >= viewWidth - binding.searchET.paddingEnd - drawableWidth) {
                            // Perform your action here
                            Toast.makeText(
                                requireContext(),
                                "DrawableEnd clicked",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnTouchListener true
                        }
                    }
                }
            }
            false
        }
    }

    private fun fetchCardsListing() {

        loadJSONFromAsset()?.let { mainViewModel.loadData(it) }
        //   mainViewModel.fetchLatestResults()
    }

    fun loadJSONFromAsset(): String? {

        var json: String? = null
        try {
            val inStreem = requireContext().assets.open("jobs_result.json")

            val size = inStreem.available()

            val buffer = ByteArray(size)

            inStreem.read(buffer)

            inStreem.close()

            json = String(buffer, Charset.defaultCharset())


        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }

    private fun setupObserver() {
        mainViewModel.deckOfCards.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    CoroutineScope(Dispatchers.Main).launch() {
                        binding.progressBar.visibility = View.GONE
                        binding.currencyRecyclerView.visibility = View.VISIBLE
                        val decOfCardsObject = it.data?.results
                        if (decOfCardsObject != null) {
                            renderCardsList(decOfCardsObject as MutableList<CarouselItem>)
                        }
                    }
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.currencyRecyclerView.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun renderCardsList(cardsList: MutableList<CarouselItem>) {
        var finalList: MutableList<Carousels> = mutableListOf()
        var tvCarousal = Carousels()
        var movieCarousal = Carousels()
        var otherCarousal = Carousels()
        cardsList.forEach {
            if(it.mediaType.toString() == MediaTypes.TV.identifier) {
                tvCarousal.mediaType = it.mediaType.toString()
                tvCarousal.results.add(it)
            }
            else if(it.mediaType.toString() == MediaTypes.MOVIE.identifier) {
                movieCarousal.mediaType = it.mediaType.toString()
                movieCarousal.results.add(it)
            }
            else {
                otherCarousal.mediaType = it.mediaType.toString()
                otherCarousal.results.add(it)
            }

        }
        finalList.add(tvCarousal)
        finalList.add(movieCarousal)
        finalList.add(otherCarousal)

        cardsAdapter.updateData(finalList)
        //cardsAdapter.updateData(cardsList)
    }

    override fun onCLick(carouselItem: CarouselItem) {
        val bundle = Bundle()
        bundle.putParcelable(CARD_PARAM, carouselItem)

        val navHostFragment =
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment) as HomeListingFragment
        val navController = navHostFragment.nav_host_fragment
        navController.findNavController().navigate(R.id.cardFragment, bundle)
    }

}
