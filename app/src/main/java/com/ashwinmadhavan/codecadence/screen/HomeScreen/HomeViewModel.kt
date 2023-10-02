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

    private val defaultGoalHours = mapOf(
        "Work" to 10.0,
        "Personal" to 10.0,
        "Study" to 10.0,
        "Other" to 10.0
    )
    private val _goalHoursMap = mutableMapOf<String, MutableLiveData<Double>>()
    init {
        // Initialize LiveData for each category
        for (category in Constants.CATEGORIES) {
            _totalHoursMap[category] = MutableLiveData()
            fetchTotalHoursForCategory(category)
        }

        for (category in Constants.CATEGORIES) {
            _goalHoursMap[category] = MutableLiveData(defaultGoalHours[category] ?: 10.0)
        }
    }

    private fun fetchTotalHoursForCategory(category: String) {
        logDao.getTotalHoursForCategory(category).observeForever { totalHours ->
            _totalHoursMap[category]?.value = totalHours
        }
    }

    fun setGoalHours(category: String, goalHours: Double) {
        _goalHoursMap[category]?.value = goalHours
    }

    fun getGoalHours(category: String): LiveData<Double> {
        return _goalHoursMap[category] ?: MutableLiveData(10.0) // Default value if not found
    }
}