package com.febatis.openstreetmapexemplo

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.appcompat.app.AppCompatActivity
import com.febatis.openstreetmapexemplo.databinding.ActivityMainBinding
import com.febatis.openstreetmapexemplo.databinding.ActivityMapBinding
import com.febatis.openstreetmapexemplo.databinding.ActivityMapWithSearchBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingMap: ActivityMapBinding
    private lateinit var bindingMapWithSearch: ActivityMapWithSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        bindingMapWithSearch = ActivityMapWithSearchBinding.inflate(layoutInflater)
        setContentView(bindingMapWithSearch.root)

        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME

        val map = bindingMapWithSearch.map
        map.setMultiTouchControls(true)

        val startPoint = GeoPoint(-24.012928, -46.454442)
        val mapController = map.controller
        mapController.setZoom(20.0)
        mapController.setCenter(startPoint)

        val startMarker = Marker(map)

        startMarker.position = startPoint
        startMarker.isDraggable = true
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(startMarker)

        //startMarker.icon = ResourcesCompat.getDrawable(resources, R.mipmap.ic_launcher);
        startMarker.title = "Casa da Vó";

        map.invalidate()

        //val overlay = MapEventsOverlay(MapEvents())
        //map.overlays.add(overlay)

        map.setOnGenericMotionListener { _, _ ->
            val mapCenter = map.mapCenter
            true
        }

        val mapEvents = MapEvents(map) {
            bindingMapWithSearch.searchBar.setText(it)

            bindingMapWithSearch.searchBar.textView.maxLines = 2
            //bindingMap.textView.text = it
        }

        map.addMapListener(mapEvents)

        bindingMapWithSearch.searchView
            .editText
            .setOnEditorActionListener { _, _, _ ->
                bindingMapWithSearch.searchBar.setText(bindingMapWithSearch.searchView.text)
                bindingMapWithSearch.searchView.hide()
                false
            }

    }

    fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress!!, 5)
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
}

