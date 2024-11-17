package com.example.zeeshanassignment.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mylibrary.R
import com.example.mylibrary.data.model.CarouselItem
import com.example.zeeshanassignment.databinding.DetailFragmentBinding
import com.example.mylibrary.utils.Constants
import com.example.mylibrary.BuildConfig
import com.example.mylibrary.enum.MediaTypes
import com.example.zeeshanassignment.activity.VideoPlayerActivity

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

        if (carouselItemObj?.mediaType.toString() == MediaTypes.MOVIE.identifier ||
            carouselItemObj?.mediaType.toString() == MediaTypes.TV.identifier ||
            carouselItemObj?.mediaType.toString() == MediaTypes.OTHER.identifier
        ) {
            binding.playBtn.visibility = View.VISIBLE
        } else {
            binding.playBtn.visibility = View.GONE
        }
        binding.playBtn.setOnClickListener {
            startActivity(Intent(requireContext(), VideoPlayerActivity::class.java))
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

            if (!it.overview.isNullOrEmpty()) {
                binding.overviewTV.text = it.overview.toString()
            }
        }
    }

    private fun loadImageInImageView() {
        carouselItemObj?.let {
            var imgName = ""
            if (!it.backdropPath.isNullOrEmpty()) {
                imgName = it.backdropPath.toString()
            } else if (!it.posterPath.isNullOrEmpty()) {
                imgName = it.posterPath.toString()
            } else {
                imgName = it.profilePath.toString()
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
