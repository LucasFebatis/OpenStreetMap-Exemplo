package com.febatis.openstreetmapexemplo

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeocoderViewModel(private val geocoder: Geocoder) : ViewModel() {

    val address = MutableLiveData<String>()

    fun findAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            address.postValue(addresses?.get(0)?.getAddressLine(0) ?: "")
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[APPLICATION_KEY] as Application).applicationContext
                val geocoder = Geocoder(context)
                GeocoderViewModel(geocoder)
            }
        }
    }
}