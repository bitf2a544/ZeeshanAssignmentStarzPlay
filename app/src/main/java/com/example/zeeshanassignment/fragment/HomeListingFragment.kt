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
import com.example.zeeshanassignment.adapter.ListAdapter
import com.example.mylibrary.data.model.CarouselItem
import com.example.mylibrary.utils.CommonUtils
import com.example.zeeshanassignment.listener.CarouselItemClickListener
import com.example.zeeshanassignment.databinding.CarouselListFragmentBinding
import com.example.mylibrary.utils.Constants.Companion.ARG_PARAM
import com.example.mylibrary.utils.Status
import com.example.mylibrary.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeListingFragment : Fragment(), CarouselItemClickListener {

    private var _binding: CarouselListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cardsAdapter: ListAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CarouselListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpSearchField()
        setupObserver()
        //fetchDataFromJsonFile()
    }

    private fun setUpRecyclerView() {
        binding.listRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        cardsAdapter = ListAdapter(requireContext(), arrayListOf(), this)
        binding.listRV.addItemDecoration(
            DividerItemDecoration(
                binding.listRV.context,
                (binding.listRV.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.listRV.adapter = cardsAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpSearchField() {
        binding.searchET.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchET.text.toString().isNotEmpty()) {
                    mainViewModel.fetchLatestResults(binding.searchET.text.toString())
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.searchET.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (binding.searchET.text.toString().isNotEmpty()) {
                    val drawableEnd =
                        binding.searchET.compoundDrawablesRelative[2] // index 2 is for drawableEnd
                    if (drawableEnd != null) {
                        val drawableWidth = drawableEnd.bounds.width()
                        val touchX = event.x
                        val viewWidth = binding.searchET.width

                        // Check if the touch is within the drawableEnd bounds
                        if (touchX >= viewWidth - binding.searchET.paddingEnd - drawableWidth) {
                            // Perform your action here
                            mainViewModel.fetchLatestResults(binding.searchET.text.toString())
                            return@setOnTouchListener true
                        }
                    }
                }
            }
            false
        }
    }

    private fun setupObserver() {
        mainViewModel.carouselsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    CoroutineScope(Dispatchers.Main).launch() {
                        binding.progressBar.visibility = View.GONE
                        val results = it.data?.results
                        if (results?.size == 0) {
                            binding.errorTV.visibility = View.VISIBLE
                            binding.listRV.visibility = View.GONE
                        } else {
                            binding.listRV.visibility = View.VISIBLE
                            binding.errorTV.visibility = View.GONE
                        }
                        if (results != null) {
                            val carousels =
                                mainViewModel.renderCardsList(results as MutableList<CarouselItem>)
                            // Update the adapter with the list
                            cardsAdapter.updateData(carousels)
                        }
                    }
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.listRV.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fetchDataFromJsonFile() {
        mainViewModel.loadLocalJsonData(CommonUtils.loadJSONFromAsset(requireContext()))
    }

    override fun onCLick(carouselItem: CarouselItem) {
        val bundle = Bundle()
        bundle.putParcelable(ARG_PARAM, carouselItem)
        val navController = findNavController()
        navController.navigate(R.id.cardFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
