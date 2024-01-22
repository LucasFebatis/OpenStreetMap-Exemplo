package com.febatis.openstreetmapexemplo

import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent


class MyDelayedMapListener(
    wrappedListener: MapListener?,
    delay: Long,
    private val onScroll: () -> Unit
) : DelayedMapListener(wrappedListener, delay) {
    override fun onScroll(event: ScrollEvent?): Boolean {
        onScroll()
        return super.onScroll(event)
    }
}