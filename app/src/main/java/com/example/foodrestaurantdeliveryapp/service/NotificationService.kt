package com.example.foodrestaurantdeliveryapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.foodrestaurantdeliveryapp.MainActivity
import com.example.foodrestaurantdeliveryapp.R
import kotlinx.coroutines.*

class NotificationService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var notificationJob: Job? = null
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "food_delivery_channel"
    private val CHANNEL_NAME = "Food Delivery Notifications"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        
        val notification = createNotification("Сервис уведомлений запущен")

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPeriodicNotifications()
        return START_STICKY
    }

    private fun startPeriodicNotifications() {
        notificationJob = serviceScope.launch {
            while (isActive) {
                showNotification("Напоминание", "Закажите что-нибудь вкусное! 🍕")
                delay(100000000000000000L)
            }
        }
    }

    private fun showNotification(title: String, message: String) {
        val notification = createNotification(message)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        
        val notificationId = (System.currentTimeMillis() % 10000).toInt()
        notificationManager.notify(notificationId, notification)

        
        android.util.Log.d("NotificationService", "Уведомление отправлено: $message")
    }

    private fun createNotification(content: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Food Delivery App")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH  
            ).apply {
                description = "Канал для уведомлений Food Delivery"
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        notificationJob?.cancel()
        serviceScope.cancel()

        
        android.util.Log.d("NotificationService", "Сервис остановлен")
    }
}