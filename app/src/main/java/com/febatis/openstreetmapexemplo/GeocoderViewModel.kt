package com.febatis.openstreetmapexemplo

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.location.GeocoderNominatim
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import java.io.IOException

class GeocoderViewModel(private val geocoder: Geocoder) : ViewModel() {

    val address = MutableLiveData<String>()
    val geoPoint = MutableLiveData<List<Address>?>()

    fun findAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            address.postValue(addresses?.get(0)?.getAddressLine(0) ?: "")
        }
    }

    fun findAddressByText(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            //val addresses = geocoder.getFromLocationName(text, 5)
            val geocoderNominatim = GeocoderNominatim(BuildConfig.LIBRARY_PACKAGE_NAME)
            val addresses = getAddressesFromText(text)
            geoPoint.postValue(addresses)
        }
    }


    private fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = geocoder.getFromLocationName(strAddress!!, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            location.latitude
            location.longitude
            p1 = GeoPoint(
                (location.latitude * 1E6),
                (location.longitude * 1E6)
            )
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    private fun getAddressesFromText(strAddress: String): List<Address>? {
        try {
            return geocoder.getFromLocationName(strAddress, 5)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
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