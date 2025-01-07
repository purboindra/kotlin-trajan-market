package com.example.trajanmarket.ui.screens.maps

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.trajanmarket.ui.screens.register.RegisterViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OsmdroidMapScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    navHostController: NavHostController,
    longitude: Double,
    latitude: Double,
    label: String
) {
    val context = LocalContext.current

    Configuration.getInstance().apply {
        userAgentValue = context.packageName
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setBuiltInZoomControls(true)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(latitude, longitude))

            }

            val marker = Marker(mapView).apply {
                position = GeoPoint(latitude, longitude)
                title = label
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                isDraggable = true
                setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                    override fun onMarkerDragStart(marker: Marker?) {
                        Log.d("MarkerDrag", "Drag started at: ${marker?.position}")
                    }

                    override fun onMarkerDrag(marker: Marker?) {
                        Log.d("MarkerDrag", "Dragging to: ${marker?.position}")
                    }

                    override fun onMarkerDragEnd(marker: Marker?) {
                        marker?.let {
                            val updatedPosition = it.position
                            Log.d(
                                "MarkerDrag",
                                "Drag ended at: ${updatedPosition.latitude}, ${updatedPosition.longitude}"
                            )
                            registerViewModel.updateMarkerPosition(
                                it.position.latitude,
                                it.position.longitude
                            )
                        }
                    }
                })
            }
            mapView.overlays.add(marker)

            mapView
        }
    )

}
