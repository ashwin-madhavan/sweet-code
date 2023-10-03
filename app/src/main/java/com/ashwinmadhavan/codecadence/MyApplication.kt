package com.ashwinmadhavan.codecadence

import android.app.Application
import androidx.room.Room
import com.ashwinmadhavan.codecadence.data.LogDatabase

class MyApplication : Application() {
    companion object {
        lateinit var logDatabase: LogDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        logDatabase = Room.databaseBuilder(
            applicationContext,
            LogDatabase::class.java, "log-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
