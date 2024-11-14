package com.example.zeeshanassignment.viewmodel

import androidx.lifecycle.*
import com.example.zeeshanassignment.repository.MainRepository
import com.example.zeeshanassignment.utils.Resource
import com.example.zeeshanassignment.data.model.Carousels
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _deckOfCards = MutableLiveData<Resource<Carousels>>()
    val deckOfCards: LiveData<Resource<Carousels>> get() = _deckOfCards

    fun fetchLatestResults() {
        _deckOfCards.postValue(Resource.loading(null))
        CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                try {
                    if (mainRepository.isNetworkAvailable()) {
                        mainRepository.getLatestResultsFromNetwork("3d0cda4466f269e793e9283f6ce0b75e","jobs")
                            .let {
                                if (it.isSuccessful) {
                                    _deckOfCards.postValue(Resource.success(it.body()))
                                } else {
                                    _deckOfCards.postValue(
                                        Resource.error(
                                            it.errorBody().toString(), null
                                        )
                                    )
                                }
                            }
                    } else _deckOfCards.postValue(
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
          _deckOfCards.postValue(Resource.loading(null))
          CoroutineScope(Dispatchers.IO).launch {
              viewModelScope.launch {
                  try {
                      if (mainRepository.isNetworkAvailable()) {
                          val detail = Gson().fromJson(jsonStringFromAssets, Carousels::class.java)
                          delay(1000)
                          _deckOfCards.postValue(Resource.success(detail))
                      } else _deckOfCards.postValue(
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


    fun getDeckOfCardsMutableLiveData(): LiveData<Resource<Carousels>?> {
        return _deckOfCards
    }

}