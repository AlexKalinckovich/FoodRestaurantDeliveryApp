package com.example.foodrestaurantdeliveryapp

import android.app.Application
import com.example.foodrestaurantdeliveryapp.BuildConfig
import io.github.imagekit.android.ImageKit
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageKit.getInstance().init(
            publicKey = BuildConfig.IMAGEKIT_PUBLIC_KEY,
            urlEndpoint = BuildConfig.IMAGEKIT_URL_ENDPOINT,
            context = applicationContext
        )
    }
}