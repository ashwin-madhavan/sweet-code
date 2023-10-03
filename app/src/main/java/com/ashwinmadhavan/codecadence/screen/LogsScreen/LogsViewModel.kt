package com.ashwinmadhavan.codecadence.screen.LogsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashwinmadhavan.codecadence.MyApplication
import com.ashwinmadhavan.codecadence.data.LogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class LogsViewModel : ViewModel() {
    private val logDao = MyApplication.logDatabase.logDao()
    private val _allLogs = MutableLiveData<List<LogEntity>>()
    val allLogs: LiveData<List<LogEntity>> get() = _allLogs


    init {
        logDao.getAllLogs().observeForever {
            _allLogs.postValue(it)
        }
    }

    fun insertLog(
        category: String,
        date: String,
        totalHours: Double,
        notes: String
    ) {
        val logEntity = LogEntity(
            category = category,
            date = date,
            totalHours = totalHours,
            notes = notes
        )

        viewModelScope.launch(Dispatchers.IO) {
            logDao.insertLog(logEntity)
            _allLogs.postValue(logDao.getAllLogs().value)
        }
    }

    fun deleteAllLogs() {
        viewModelScope.launch(Dispatchers.IO) {
            logDao.deleteAllLogs()
        }
    }
}
