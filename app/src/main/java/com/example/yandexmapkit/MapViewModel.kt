package com.example.yandexmapkit

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

class MapViewModel : ViewModel() {

    private val _cameraPositionUpdate = MutableLiveData<CameraPosition>()
    val cameraPositionUpdate: LiveData<CameraPosition> = _cameraPositionUpdate

    private val _selectedPoints = MutableLiveData<List<Point>>(emptyList())
    val selectedPoints: LiveData<List<Point>> = _selectedPoints

    private val _drivingRoute = MutableLiveData<DrivingRoute?>()
    val drivingRoute: LiveData<DrivingRoute?> = _drivingRoute

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private var drivingRouter: DrivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
    private var drivingSession: DrivingSession? = null

    init {
        _cameraPositionUpdate.value = CameraPosition(Point(0.0, 0.0), 2.0f, 0.0f, 0.0f)
    }

    fun moveToKemerovo() {
        _cameraPositionUpdate.value = CameraPosition(Point(55.354993, 86.085805), 15.0f, 0.0f, 0.0f)
    }

    fun moveToLocation(location: Location) {
        _cameraPositionUpdate.value = CameraPosition(Point(location.latitude, location.longitude), 15.0f, 0.0f, 0.0f)
    }

    fun addPointAndBuildRoute(newPoint: Point) {
        val currentPoints = _selectedPoints.value?.toMutableList() ?: mutableListOf()

        if (currentPoints.size < 2) {
            currentPoints.add(newPoint)
            _selectedPoints.value = currentPoints
            if (currentPoints.size == 2) {
                buildRoute(currentPoints[0], currentPoints[1])
            }
        } else {
            clearPointsAndRouteInternal()
            currentPoints.clear()
            currentPoints.add(newPoint)
            _selectedPoints.value = currentPoints
            _toastMessage.value = "Выбрана первая точка нового маршрута."
        }
    }

    private fun buildRoute(start: Point, end: Point) {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        val requestPoints = arrayListOf(
            RequestPoint(start, RequestPointType.WAYPOINT, null, null),
            RequestPoint(end, RequestPointType.WAYPOINT, null, null)
        )

        drivingSession?.cancel()

        drivingSession = drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            object : DrivingSession.DrivingRouteListener {
                override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
                    if (routes.isNotEmpty()) {
                        _drivingRoute.value = routes.first()
                        _toastMessage.value = "Маршрут построен"
                    } else {
                        _drivingRoute.value = null
                        _toastMessage.value = "Маршруты не найдены"
                    }
                }

                override fun onDrivingRoutesError(error: Error) {
                    _drivingRoute.value = null
                    val errorMessage = when (error) {
                        is RemoteError -> "Ошибка сервера при построении маршрута"
                        is NetworkError -> "Ошибка сети при построении маршрута"
                        else -> "Неизвестная ошибка при построении маршрута"
                    }
                    _toastMessage.value = errorMessage
                }
            }
        )
    }

    fun clearPointsAndRoute() {
        clearPointsAndRouteInternal()
        _toastMessage.value = "Точки и маршрут очищены"
    }

    private fun clearPointsAndRouteInternal() {
        _selectedPoints.value = emptyList()
        _drivingRoute.value = null
        drivingSession?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        drivingSession?.cancel()
    }
}
