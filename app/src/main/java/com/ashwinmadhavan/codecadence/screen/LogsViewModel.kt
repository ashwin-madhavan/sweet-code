package com.ashwinmadhavan.codecadence.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashwinmadhavan.codecadence.MyApplication
import com.ashwinmadhavan.codecadence.data.LogEntity
import com.ashwinmadhavan.codecadence.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class LogsViewModel : ViewModel() {

    private val userDao = MyApplication.database.userDao()
    private val _allUsers = MutableLiveData<List<User>>() // Private mutable LiveData
    val allUsers: LiveData<List<User>> get() = _allUsers // Public LiveData

    private val logDao = MyApplication.logDatabase.logDao()
    private val _allLogs = MutableLiveData<List<LogEntity>>()
    val allLogs: LiveData<List<LogEntity>> get() = _allLogs


    init {
        // Observe changes in the userDao and update the LiveData
        userDao.getAll().observeForever {
            _allUsers.postValue(it)
        }

        logDao.getAllLogs().observeForever {
            _allLogs.postValue(it)
        }
    }

    // Use viewModelScope to automatically handle lifecycle and cancellation
    fun insertUser(firstName: String, lastName: String) {
        val user = User(firstName = firstName, lastName = lastName)
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertAll(user) // Insert a single user, so use insertAll with a single user
            Log.d("INSERT TAG", user.uid.toString())

            // After insertion, update the LiveData
            _allUsers.postValue(userDao.getAll().value)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteAllUsers()
        }
    }

    fun insertLog(
        category: String,
        date: Date,
        startTime: String,
        endTime: String,
        totalHours: Double,
        notes: String
    ) {
        val logEntity = LogEntity(
            category = category,
            date = date,  // Use appropriate date
            startTime = startTime,
            endTime = endTime,
            totalHours = totalHours,
            notes = notes
        )

        viewModelScope.launch(Dispatchers.IO) {
            logDao.insertLog(logEntity) // Insert a single user, so use insertAll with a single user

            // After insertion, update the LiveData
            _allLogs.postValue(logDao.getAllLogs().value)
        }
    }

    fun deleteAllLogs() {
        viewModelScope.launch(Dispatchers.IO) {
            logDao.deleteAllLogs()
        }
    }
}
