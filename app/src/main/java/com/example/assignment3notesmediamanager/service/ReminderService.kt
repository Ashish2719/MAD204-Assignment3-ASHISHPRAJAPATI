/**
 * Course: MAD204 - Assignment 3
 * Student Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: Background Service that waits a few seconds and then shows a notification "Don't forget your notes!" which opens MainActivity when tapped.
 */

package com.example.assignment3notesmediamanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.assignment3notesmediamanager.MainActivity
import com.example.assignment3notesmediamanager.R

class ReminderService : Service() {

    private val handler = Handler()
    private val delayMillis = 7000L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        handler.postDelayed({
            showNotification()
            stopSelf()
        }, delayMillis)
    }

    private fun showNotification() {
        val channelId = "notes_channel"
        val manager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notes Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Don't forget your notes!")
            .setContentText("Tap to open your Notes & Media Manager.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Add this line:
        android.util.Log.d("REMINDER", "Notification shown")

        manager.notify(1, notification)
    }
}