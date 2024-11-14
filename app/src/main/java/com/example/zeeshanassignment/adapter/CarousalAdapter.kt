package com.example.zeeshanassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zeeshanassignment.R
import com.example.zeeshanassignment.data.model.CarouselItem
import com.example.zeeshanassignment.databinding.CarouselItemLayoutBinding
import com.example.zeeshanassignment.ui.CarouselItemClickListener

class CarousalAdapter(
    val context: Context,
    private var cardsList: MutableList<CarouselItem>,
    private val cardItemClickListener1: CarouselItemClickListener
) :

    RecyclerView.Adapter<CarousalAdapter.DataViewHolder>() {

    lateinit var binding: CarouselItemLayoutBinding
    lateinit var itemClickListener: CarouselItemClickListener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(carouselItem: CarouselItem) {
            carouselItem?.let {
                var imgName = ""
                if (!it.backdropPath.isNullOrEmpty()) {
                    imgName = it.backdropPath.toString()
                } else if (!it.posterPath.isNullOrEmpty()) {
                    imgName = it.posterPath.toString()
                } else {
                    imgName = it.profilePath.toString()
                }

                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/original/" + imgName) // image url
                    .placeholder(R.drawable.unknow_image)
                    .error(R.drawable.unknow_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //.fitCenter()
                    .into(binding.imageTV);
            }
            binding.imageTV.setOnClickListener {
                itemClickListener.onCLick(carouselItem)
            };


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = CarouselItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        );
        itemClickListener = cardItemClickListener1
        return DataViewHolder(binding.root);
    }

    override fun getItemCount(): Int = cardsList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(cardsList[position])

    fun updateData(list: MutableList<CarouselItem>) {
        cardsList = mutableListOf()
        cardsList = list;
        notifyDataSetChanged()
    }

}