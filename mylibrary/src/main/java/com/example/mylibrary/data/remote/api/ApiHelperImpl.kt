package com.example.mylibrary.data.remote.api

import com.example.mylibrary.data.model.Carousels
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getLatestResultsFromAPI(apiKey: String? , query: String?): Response<Carousels> =
        apiService.getSearchResults(apiKey,query)
}