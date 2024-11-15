package com.example.mylibrary.repository

import com.example.mylibrary.data.remote.api.ApiHelper
import com.example.mylibrary.utils.NetworkHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val networkHelper: NetworkHelper
) {

    suspend fun getLatestResultsFromNetwork(apiKey: String? , query: String?) =
        apiHelper.getLatestResultsFromAPI(apiKey,query)

    fun isNetworkAvailable(): Boolean {
       return networkHelper.isNetworkConnected()
    }

}