package com.example.foodrestaurantdeliveryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val publicKey: String = getString(R.string.imagekit_public_key)
        val urlEndpoint: String = getString(R.string.imagekit_url_endpoint)

//        ImageKit.init(
//            context = applicationContext,
//            publicKey = publicKey,
//            urlEndpoint = urlEndpoint,
//        )
    }
}