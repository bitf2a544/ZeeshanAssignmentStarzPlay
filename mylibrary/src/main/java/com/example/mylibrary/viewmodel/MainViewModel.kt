package com.example.mylibrary.viewmodel

import androidx.lifecycle.*
import com.example.mylibrary.BuildConfig
import com.example.mylibrary.data.model.CarouselItem
import com.example.mylibrary.repository.MainRepository
import com.example.mylibrary.utils.Resource
import com.example.mylibrary.data.model.Carousels
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _carouselsMutableLiveData = MutableLiveData<Resource<Carousels>>()
    val carouselsLiveData: LiveData<Resource<Carousels>> get() = _carouselsMutableLiveData

    fun fetchLatestResults(query: String) {
        _carouselsMutableLiveData.postValue(Resource.loading(null))
        CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                try {
                    if (mainRepository.isNetworkAvailable()) {
                        mainRepository.getLatestResultsFromNetwork(BuildConfig.API_KEY, query)
                            .let {
                                if (it.isSuccessful) {
                                    _carouselsMutableLiveData.postValue(Resource.success(it.body()))
                                } else {
                                    _carouselsMutableLiveData.postValue(
                                        Resource.error(
                                            it.errorBody().toString(), null
                                        )
                                    )
                                }
                            }
                    } else _carouselsMutableLiveData.postValue(
                        Resource.error(
                            "No internet connection",
                            null
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun loadData(jsonStringFromAssets: String) {
        _carouselsMutableLiveData.postValue(Resource.loading(null))
        CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                try {
                    if (mainRepository.isNetworkAvailable()) {
                        val detail = Gson().fromJson(jsonStringFromAssets, Carousels::class.java)
                        delay(1000)
                        _carouselsMutableLiveData.postValue(Resource.success(detail))
                    } else _carouselsMutableLiveData.postValue(
                        Resource.error(
                            "No internet connection",
                            null
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun renderCardsList(cardsList: MutableList<CarouselItem>): MutableList<Carousels> {
        // Map to group CarouselItems by mediaType
        val map = mutableMapOf<String, Carousels>()

        // Group the items into their respective media types
        cardsList.forEach { item ->
            val mediaType = item.mediaType.toString()

            // If the group for this media type doesn't exist, create a new one
            val carousal =
                map.getOrPut(mediaType) { Carousels().apply { this.mediaType = mediaType } }
            carousal.results.add(item)
        }

        // Convert the map values to a list for the adapter
        return map.values.toMutableList()
    }

    fun getCarouselsMutableLiveData(): LiveData<Resource<Carousels>?> {
        return _carouselsMutableLiveData
    }

}