package com.example.zeeshanassignment.data.remote.api

import com.example.zeeshanassignment.data.model.Carousels
import retrofit2.Response

interface ApiHelper {
    suspend fun getLatestResultsFromAPI(apiKey: String? , query: String?): Response<Carousels>
}