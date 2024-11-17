package com.example.mylibrary.data.remote.api

import com.example.mylibrary.data.model.Carousels
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("3/search/multi")
    suspend fun getSearchResults(
        @Query("api_key", encoded = true) apiKey: String?,
        @Query("query", encoded = true) queryParam: String?
    ): Response<Carousels>

}