package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashwinmadhavan.codecadence.Constants
import com.ashwinmadhavan.codecadence.MyApplication

class HomeViewModel : ViewModel() {
    private val logDao = MyApplication.logDatabase.logDao()

    private val _totalHoursMap = mutableMapOf<String, MutableLiveData<Double>>()
    val totalHoursMap: Map<String, LiveData<Double>> get() = _totalHoursMap

    init {
        // Initialize LiveData for each category
        for (category in Constants.CATEGORIES) {
            _totalHoursMap[category] = MutableLiveData()
            fetchTotalHoursForCategory(category)
        }
    }

    private fun fetchTotalHoursForCategory(category: String) {
        logDao.getTotalHoursForCategory(category).observeForever { totalHours ->
            _totalHoursMap[category]?.value = totalHours
        }
    }
}