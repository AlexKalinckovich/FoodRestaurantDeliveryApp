package com.example.foodrestaurantdeliveryapp.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

object NotificationHelper {

    fun startService(context: Context) {
        val intent = Intent(context, NotificationService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }

        Log.d("NotificationHelper", "Сервис запущен")
    }

    fun stopService(context: Context) {
        val intent = Intent(context, NotificationService::class.java)
        context.stopService(intent)

        Log.d("NotificationHelper", "Сервис остановлен")
    }
}