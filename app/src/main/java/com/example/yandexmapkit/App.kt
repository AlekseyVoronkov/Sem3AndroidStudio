package com.example.yandexmapkit

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("c4efd126-a267-4515-8a1a-9f00417925a7")
        MapKitFactory.initialize(this)
    }
}