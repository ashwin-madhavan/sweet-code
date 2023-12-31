package com.ashwinmadhavan.codecadence.screen.LogsScreen.Timer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Domain {

    var formattedTime by mutableStateOf("00:00.00")

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isRun = false
    private var timeMillis = 0L
    private var lastTimestamp = 0L

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        if (isRun) return
        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            isRun = true
            while (isRun) {
                delay(30L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(timeMillis),
                    ZoneId.systemDefault()
                ).format(
                    DateTimeFormatter.ofPattern(
                        "mm:ss.SS",
                        Locale.getDefault()
                    )
                )
            }
        }
    }

    fun pause() {
        isRun = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimestamp = 0L
        formattedTime = "00:00.00"
        isRun = false
    }
}