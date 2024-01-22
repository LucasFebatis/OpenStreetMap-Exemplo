package com.febatis.openstreetmapexemplo

import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent


class MyMapListener(private val onScroll: () -> Unit) : MapListener {
    override fun onScroll(event: ScrollEvent?): Boolean {
        onScroll()
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return true
    }
}