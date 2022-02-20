package com.example.todoapp_mvc.local.database

import android.content.Context
import androidx.room.Room

object AppDatabaseBuilder {
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "data"
        )
            .createFromAsset("Data.db")
            .allowMainThreadQueries()
            .build()
}