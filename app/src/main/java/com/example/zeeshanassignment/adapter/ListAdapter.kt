package com.example.zeeshanassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zeeshanassignment.listener.CarouselItemClickListener

import com.example.mylibrary.data.model.Carousels
import com.example.mylibrary.enum.MediaTypes
import com.example.zeeshanassignment.databinding.ListItemLayoutBinding

class ListAdapter(
    val context: Context,
    private var cardsList: MutableList<Carousels>,
    private val cardItemClickListener1: CarouselItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var cardItemClickListener: CarouselItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            MediaTypes.OTHER.intIdentifier -> {
                val binding = ListItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                );
                cardItemClickListener = cardItemClickListener1
                return OtherViewHolder(binding.root, binding);
            }

            else -> {
                return object : RecyclerView.ViewHolder(View(context)) {}
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OtherViewHolder -> {
                holder.bindView(cardsList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return MediaTypes.OTHER.intIdentifier
    }

    override fun getItemCount(): Int = cardsList.size

    fun updateData(list: MutableList<Carousels>) {
        cardsList = mutableListOf()
        cardsList = list;
        notifyDataSetChanged()
    }

    inner class OtherViewHolder(
        view: View,
        private var itemBinding: ListItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {
        fun bindView(item: Carousels) {
            if (!item.mediaType.isNullOrEmpty())
                itemBinding.titleTV.text = item.mediaType
            else
                itemBinding.titleTV.text = "None"
            setUpCurrencyRecyclerView(item)
        }
        private fun setUpCurrencyRecyclerView(item: Carousels) {
            itemBinding.carouselRV.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val cardsAdapter = CarousalAdapter(context, arrayListOf(), cardItemClickListener)
            cardsAdapter.updateData(item.results)
            itemBinding.carouselRV.adapter = cardsAdapter
        }
    }
}