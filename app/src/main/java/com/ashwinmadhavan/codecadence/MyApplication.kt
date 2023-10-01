package com.ashwinmadhavan.codecadence

import android.app.Application
import androidx.room.Room
import com.ashwinmadhavan.codecadence.data.AppDatabase
import com.ashwinmadhavan.codecadence.data.LogDatabase

class MyApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
        lateinit var logDatabase: LogDatabase
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

        logDatabase = Room.databaseBuilder(
            applicationContext,
            LogDatabase::class.java, "log-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}
