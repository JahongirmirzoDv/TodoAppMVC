package com.example.todoapp_mvc.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.todoapp_mvc.R

class MyReceiver : BroadcastReceiver() {
    private val TAG = "MyReceiver"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: taskComplete")

        val taskName = intent?.getStringExtra("taskName")
        val defaultSoundUri: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, "channelId")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("task should be done")
        builder.setContentText(taskName)
        builder.setSound(defaultSoundUri, AudioManager.STREAM_NOTIFICATION)
        builder.setAutoCancel(true)
        val notification = builder.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("channelId", "Name", importance)
            mChannel.description = "descriptionText"
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(1, notification)
    }
}