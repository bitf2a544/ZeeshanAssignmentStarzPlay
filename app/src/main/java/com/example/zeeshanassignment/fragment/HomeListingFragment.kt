package com.example.zeeshanassignment.fragment


import android.annotation.SuppressLint
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
import com.example.mylibrary.R
import com.example.mylibrary.adapter.ListAdapter
import com.example.mylibrary.data.model.CarouselItem
import com.example.mylibrary.data.model.Carousels
import com.example.mylibrary.enum.MediaTypes
import com.example.mylibrary.CarouselItemClickListener
import com.example.mylibrary.databinding.CarouselListFragmentBinding
import com.example.mylibrary.utils.Constants.Companion.ARG_PARAM
import com.example.mylibrary.utils.Status
import com.example.mylibrary.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.Charset


@AndroidEntryPoint
class HomeListingFragment : Fragment(), CarouselItemClickListener {
    private lateinit var binding: CarouselListFragmentBinding
    private lateinit var cardsAdapter: ListAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CarouselListFragmentBinding.inflate(layoutInflater)
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
        cardsAdapter = ListAdapter(requireContext(), arrayListOf(), this)
        binding.currencyRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.currencyRecyclerView.context,
                (binding.currencyRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.currencyRecyclerView.adapter = cardsAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
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

/*    private fun renderCardsList(cardsList: MutableList<CarouselItem>) {
        val finalList: MutableList<Carousels> = mutableListOf()
        val tvCarousal = Carousels()
        val movieCarousal = Carousels()
        val otherCarousal = Carousels()
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
    }*/

    private fun renderCardsList(cardsList: MutableList<CarouselItem>) {
        // Map to group CarouselItems by mediaType
        val map = mutableMapOf<String, Carousels>()

        // Group the items into their respective media types
        cardsList.forEach { item ->
            val mediaType = item.mediaType.toString()

            // If the group for this media type doesn't exist, create a new one
            val carousal = map.getOrPut(mediaType) { Carousels().apply { this.mediaType = mediaType } }
            carousal.results.add(item)
        }

        // Convert the map values to a list for the adapter
        val finalList = map.values.toMutableList()

        // Update the adapter with the grouped list
        cardsAdapter.updateData(finalList)
    }

    override fun onCLick(carouselItem: CarouselItem) {
        val bundle = Bundle()
        bundle.putParcelable(ARG_PARAM, carouselItem)
        val navController = findNavController()
        navController.navigate(R.id.cardFragment, bundle)
    }

}
