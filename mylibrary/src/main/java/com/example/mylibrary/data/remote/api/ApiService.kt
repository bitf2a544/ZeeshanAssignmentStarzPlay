package com.example.mylibrary.data.remote.api

import com.example.mylibrary.data.model.Carousels
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //3/search/multi?api_key=3d0cda4466f269e793e9283f6ce0b75e&query=jobs

    @GET("3/search/multi")
    suspend fun getSearchResults(@Query("api_key", encoded = true) apiKey: String?,
                                 @Query("query", encoded = true) queryParam: String?): Response<Carousels>


/*    @GET("api/deck/new/draw/")
    suspend fun getLatestDecOfCards(
        @Query(
            "count",
            encoded = true
        ) count: Int?
    ): Response<DeckOfCards>*/
}