package com.example.yandexmapkit

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.example.yandexmapkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MapViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var placemarkMapObjects: MapObjectCollection? = null
    private var routePolylineObject: PolylineMapObject? = null

    private val mapInputListener = object : InputListener {
        override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
        }

        override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
            viewModel.addPointAndBuildRoute(point)
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentUserLocation()
        } else {
            Toast.makeText(this, "Разрешение на местоположение не предоставлено", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        placemarkMapObjects = binding.mapview.map.mapObjects.addCollection()

        setupMapListener()
        setupButtonListeners()
        observeViewModel()
    }

    private fun setupMapListener() {
        binding.mapview.map.addInputListener(mapInputListener)
    }

    private fun setupButtonListeners() {
        binding.btnZoomKemerovo.setOnClickListener {
            viewModel.moveToKemerovo()
        }
        binding.btnZoomMyLocation.setOnClickListener {
            requestLocationPermission()
        }
        binding.btnClear.setOnClickListener {
            viewModel.clearPointsAndRoute()
        }
    }

    private fun observeViewModel() {
        viewModel.cameraPositionUpdate.observe(this) { cameraPosition ->
            binding.mapview.map.move(
                cameraPosition,
                Animation(Animation.Type.SMOOTH, 0.5f),
                null
            )
        }

        viewModel.selectedPoints.observe(this) { points ->
            placemarkMapObjects?.clear()
            points.forEach { point ->
                val placemark = placemarkMapObjects?.addPlacemark(point)
                placemark?.opacity = 0.8f
            }
        }

        viewModel.drivingRoute.observe(this) { route ->
            routePolylineObject?.let { binding.mapview.map.mapObjects.remove(it) }
            routePolylineObject = null

            if (route != null) {
                routePolylineObject = binding.mapview.map.mapObjects.addPolyline(route.geometry)
                routePolylineObject?.setStrokeColor(Color.BLUE)
                routePolylineObject?.strokeWidth = 3f
            }
        }

        viewModel.toastMessage.observe(this) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentUserLocation()
            }
            else -> {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getCurrentUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        viewModel.moveToLocation(location)
                    } else {
                        Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ошибка получения местоположения", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}
