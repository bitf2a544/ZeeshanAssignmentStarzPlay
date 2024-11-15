package com.example.mylibrary

import com.example.mylibrary.data.model.CarouselItem

interface CarouselItemClickListener {
    fun onCLick(deckCard: CarouselItem);
}
