package com.example.mylibrary.data.remote.api

import com.example.mylibrary.data.model.Carousels
import retrofit2.Response

interface ApiHelper {
    suspend fun getLatestResultsFromAPI(apiKey: String? , query: String?): Response<Carousels>
}