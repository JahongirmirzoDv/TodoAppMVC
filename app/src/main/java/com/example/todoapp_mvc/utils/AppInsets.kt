package com.example.todoapp_mvc.utils

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object AppInsets {
    @RequiresApi(Build.VERSION_CODES.R)
    fun set(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(
            view
        ) { v: View, windowInsets: WindowInsetsCompat ->
            val insets: Insets =
                windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, insets.top, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }
}