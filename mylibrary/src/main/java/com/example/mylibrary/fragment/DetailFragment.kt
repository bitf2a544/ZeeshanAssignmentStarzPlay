package com.example.mylibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mylibrary.BuildConfig
import com.example.mylibrary.R
import com.example.mylibrary.data.model.CarouselItem
import com.example.mylibrary.databinding.DetailFragmentBinding
import com.example.mylibrary.utils.Constants

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private var carouselItemObj: CarouselItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        carouselItemObj = arguments?.getParcelable(Constants.ARG_PARAM)!!

        if (carouselItemObj?.mediaType.toString() == "video" ||
            carouselItemObj?.mediaType.toString() == "tv" ||
            carouselItemObj?.mediaType.toString() == "movie"
        ) {
            binding.playBtn.visibility = View.VISIBLE
        } else {
            binding.playBtn.visibility = View.GONE
        }
        binding.playBtn.setOnClickListener {

        }
        setTextInCardView()
        loadImageInImageView()

        return binding.root
    }

    private fun setTextInCardView() {
        carouselItemObj?.let {
            if (!it.originalTitle.isNullOrEmpty())
                binding.titleTV.text = it.originalTitle
            else
                binding.titleTV.text = it.originalName
            binding.overviewTV.text = it.overview.toString()
        }
    }

    private fun loadImageInImageView() {
        carouselItemObj?.let {
            var imgName = ""
            if (!it.backdropPath.isNullOrEmpty()) {
                imgName = it.backdropPath.toString()
            } else {
                imgName = it.posterPath.toString()
            }
            Glide.with(requireContext())
                .load(BuildConfig.IMAGE_BASE_URL + imgName) // image url
                .placeholder(R.drawable.unknow_image)
                .error(R.drawable.unknow_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageTV);
        }
    }
}
