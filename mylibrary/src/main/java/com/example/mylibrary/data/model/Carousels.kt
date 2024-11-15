package com.example.mylibrary.data.model


import com.google.gson.annotations.SerializedName


data class Carousels (

  @SerializedName("page"          ) var page         : Int?               = null,
  @SerializedName("total_results" ) var totalResults : Int?               = null,
  @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
  @SerializedName("results"       ) var results      : ArrayList<CarouselItem> = arrayListOf(),
  var mediaType : String? = null,

  )