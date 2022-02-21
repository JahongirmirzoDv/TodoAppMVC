package com.example.todoapp_mvc.controller

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.todoapp_mvc.broadcast.MyReceiver
import java.util.*

class AlarmController(val context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val TAG = "AlarmController"

    fun setAlarm(id: Int, date: String, time:String, taskName: String) {
        val intent = Intent(context, MyReceiver::class.java)
        intent.putExtra("taskName", taskName)
        val broadcast = PendingIntent.getBroadcast(context, id, intent, 0)
        val calendar = Calendar.getInstance()
        val year = date.substring(6).toInt()
        val month = date.substring(3, 5).toInt().minus(1)
        val day = date.substring(0, 2).toInt()
        val hour =  time.substring(0, 2).toInt()
        val minute = time.substring(3).toInt()
        calendar.set(year, month, day, hour, minute)
        val timeInMillis = calendar.timeInMillis
        val calendar2 = Calendar.getInstance()
        Log.d(TAG, "setAlarm: $timeInMillis")
        Log.d(TAG, "setAlarm: ${calendar2.timeInMillis}")
        if (calendar2.timeInMillis > timeInMillis) {
            Toast.makeText(
                context,
                "Task deadline already expired, can't set alarm",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, broadcast)
        }
    }

    fun disableAlarm(id: Int) {
        val intent = Intent(context, MyReceiver::class.java)
        val broadcast = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.cancel(broadcast)
    }
}