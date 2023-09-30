package com.ashwinmadhavan.codecadence.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashwinmadhavan.codecadence.MyApplication
import com.ashwinmadhavan.codecadence.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestViewModel : ViewModel() {

    private val userDao = MyApplication.database.userDao()

    // Use viewModelScope to automatically handle lifecycle and cancellation
    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertAll(user) // Insert a single user, so use insertAll with a single user
            Log.d("INSERT TAG", "Insert!")
        }
    }

    // Access the database in a background thread
    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAll()
    }
}