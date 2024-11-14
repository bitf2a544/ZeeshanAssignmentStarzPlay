package com.example.zeeshanassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.zeeshanassignment.databinding.CardItemLayoutBinding
import com.example.zeeshanassignment.ui.CarouselItemClickListener
import com.example.zeeshanassignment.data.model.CarouselItem
import com.example.zeeshanassignment.data.model.Carousels
import com.example.zeeshanassignment.databinding.MovieItemLayoutBinding
import com.example.zeeshanassignment.enum.MediaTypes

class CarouselListAdapter(
    val context: Context,
    private var cardsList: MutableList<Carousels>,
    private val cardItemClickListener1: CarouselItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var cardItemClickListener: CarouselItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            MediaTypes.MOVIE.intIdentifier -> {
                val binding = MovieItemLayoutBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                return MovieViewHolder(binding.root, binding)
            }
            MediaTypes.TV.intIdentifier -> {
                val binding = MovieItemLayoutBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                return TVViewHolder(binding.root, binding)
            }
            MediaTypes.OTHER.intIdentifier -> {
                val binding = CardItemLayoutBinding.inflate(
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
           /* is MovieViewHolder -> {
                holder.bindView(cardsList[position])
            }
            is TVViewHolder -> {
                holder.bindView(cardsList[position])
            }*/
            is OtherViewHolder -> {
                holder.bindView(cardsList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        /*return if (cardsList[position].mediaType.toString() == MediaTypes.MOVIE.identifier) {
            MediaTypes.MOVIE.intIdentifier
        } else if (cardsList[position].mediaType.toString() == MediaTypes.TV.identifier) {
            MediaTypes.TV.intIdentifier
        } else {
            MediaTypes.OTHER.intIdentifier
        }*/
        return  MediaTypes.OTHER.intIdentifier
    }
    override fun getItemCount(): Int = cardsList.size

    fun updateData(list: MutableList<Carousels>) {
        cardsList = mutableListOf()
        cardsList = list;
        notifyDataSetChanged()
    }

    inner class TVViewHolder(
        view: View,
        private var itemBinding: MovieItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {
        fun bindView(item: CarouselItem) {
            if (!item.originalTitle.isNullOrEmpty())
                itemBinding.cardTV.text = item.originalTitle + " TV"
            else
                itemBinding.cardTV.text = item.originalName + " TV"
            itemBinding.cardTV.setOnClickListener {
                cardItemClickListener.onCLick(item)
            };
        }
    }

    inner class MovieViewHolder(
        view: View,
        private var itemBinding: MovieItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {
        fun bindView(item: CarouselItem) {
            if (!item.originalTitle.isNullOrEmpty())
                itemBinding.cardTV.text = item.originalTitle + " Movie"
            else
                itemBinding.cardTV.text = item.originalName + " Movie"
            itemBinding.cardTV.setOnClickListener {
                cardItemClickListener.onCLick(item)
            };
        }
    }

    inner class OtherViewHolder(
        view: View,
        private var itemBinding: CardItemLayoutBinding
    ) : RecyclerView.ViewHolder(view) {



        fun bindView(item: Carousels) {
            if (!item.mediaType.isNullOrEmpty())
                itemBinding.titleTV.text = item.mediaType
             else
                itemBinding.titleTV.text = "None"


            setUpCurrencyRecyclerView(item)

            itemBinding.titleTV.setOnClickListener {
           //     cardItemClickListener.onCLick(item)
            };

        }

        private fun setUpCurrencyRecyclerView(item: Carousels) {
            itemBinding.carouselRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
           var cardsAdapter = CarousalAdapter(context, arrayListOf(),cardItemClickListener)
           /* itemBinding.carouselRV.addItemDecoration(
                DividerItemDecoration(
                    itemBinding.carouselRV.context,
                    (itemBinding.carouselRV.layoutManager as LinearLayoutManager).orientation
                )
            )*/
            cardsAdapter.updateData(item.results)
            itemBinding.carouselRV.adapter = cardsAdapter
        }
    }


}