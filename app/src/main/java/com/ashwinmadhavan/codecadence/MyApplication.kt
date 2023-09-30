package com.ashwinmadhavan.codecadence

import android.app.Application
import androidx.room.Room
import com.ashwinmadhavan.codecadence.data.AppDatabase

class MyApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

}
