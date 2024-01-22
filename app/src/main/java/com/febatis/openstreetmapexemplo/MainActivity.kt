package com.febatis.openstreetmapexemplo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.febatis.openstreetmapexemplo.databinding.ActivityMainBinding
import com.febatis.openstreetmapexemplo.databinding.ActivityMapBinding
import com.febatis.openstreetmapexemplo.databinding.ActivityMapWithSearchBinding
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingMap: ActivityMapBinding
    private lateinit var bindingMapWithSearch: ActivityMapWithSearchBinding

    private val viewModel: GeocoderViewModel by viewModels { GeocoderViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMapWithSearch = ActivityMapWithSearchBinding.inflate(layoutInflater)
        setContentView(bindingMapWithSearch.root)

        bindingMapWithSearch.searchBar.textView.maxLines = 2

        val customAdapter = CustomAdapter(mutableListOf())
        bindingMapWithSearch.rvAddress.adapter = customAdapter
        bindingMapWithSearch.rvAddress.layoutManager = LinearLayoutManager(this)

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


        val myMapListener = MyMapListener {
            viewModel.findAddress(map.mapCenter.latitude, map.mapCenter.longitude)
        }

        val myDelayedMapListener = MyDelayedMapListener(myMapListener, 50) {
            bindingMapWithSearch.searchBar.setText("Carregando...")
        }

        viewModel.address.observe(this) {
            bindingMapWithSearch.searchBar.setText(it)
            //bindingMap.textView.text = it
        }

        viewModel.geoPoint.observe(this) {
            Log.i("Test", it?.size.toString())
            val listString = it?.map { a -> a.getAddressLine(0) }
            Log.i("Test", listString.toString())
            listString?.let { it1 -> customAdapter.updateList(it1) }
            bindingMapWithSearch.progress.visibility = View.INVISIBLE
        }

        map.addMapListener(myDelayedMapListener)

        bindingMapWithSearch.searchView
            .editText
            .setOnEditorActionListener { _, _, _ ->
                bindingMapWithSearch.searchBar.setText(bindingMapWithSearch.searchView.text)
                bindingMapWithSearch.searchView.hide()
                false
            }

        bindingMapWithSearch.searchView
            .editText
            .addTextChangedListener {
                bindingMapWithSearch.progress.visibility = View.VISIBLE
                viewModel.findAddressByText(it.toString())
            }

    }
}

