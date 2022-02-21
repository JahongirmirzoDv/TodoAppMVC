package com.example.todoapp_mvc.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPref {
    lateinit var sharedPreferences: SharedPreferences

    fun getInstanceDis(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            "" +
                    "", MODE_PRIVATE
        )
    }

    var data: String?
        get() = sharedPreferences.getString("data", null)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("data", value)
            }
        }
}