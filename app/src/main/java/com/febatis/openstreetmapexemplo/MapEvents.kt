package com.febatis.openstreetmapexemplo

import android.location.Geocoder
import android.util.Log
import android.widget.TextView
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.views.MapView
import java.util.Locale


class MapEvents(private val map: MapView, private val onAddress: (String) -> Unit) : MapListener {
    override fun onScroll(event: ScrollEvent?): Boolean {
        Log.i("Tag", "onScroll: ${map.mapCenter.latitude} and ${map.mapCenter.longitude}")
        onAddress("Coordenadas:\nlatitude: ${map.mapCenter.latitude}\nlongitude: ${map.mapCenter.longitude}")

        val geocoder = Geocoder(map.context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(map.mapCenter.latitude, map.mapCenter.longitude, 1)

        onAddress(addresses?.get(0)?.getAddressLine(0) ?: "")

        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        Log.i("Tag", "onZoom")
        return true
    }
}