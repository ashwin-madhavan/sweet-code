package com.ashwinmadhavan.codecadence.screen.HomeScreen

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashwinmadhavan.codecadence.Constants
import com.ashwinmadhavan.codecadence.MyApplication

class HomeViewModel(private val context: Context) : ViewModel() {
    private val logDao = MyApplication.logDatabase.logDao()

    private val _totalHoursMap = mutableMapOf<String, MutableLiveData<Double>>()
    val totalHoursMap: Map<String, LiveData<Double>> get() = _totalHoursMap

    private val _totalHours = logDao.getTotalHoursLiveData()
    private val totalHoursLiveData = MutableLiveData<Double>()
    val totalHours: LiveData<Double>
        get() = totalHoursLiveData

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    private val defaultGoalHoursKey = "defaultGoalHours"
    private val defaultGoalHours = mapOf(
        "Work" to 10.0,
        "Array" to 5.0,
        "Stack" to 20.0,
        "Binary Tree" to 30.0
    )

    private val _goalHoursMap = mutableMapOf<String, MutableLiveData<Double>>()

    init {
        // Initialize LiveData for each category
        for (category in Constants.CATEGORIES) {
            _totalHoursMap[category] = MutableLiveData()
            fetchTotalHoursForCategory(category)
        }

        loadDefaultGoalHours()


        _totalHours.observeForever { value ->
            totalHoursLiveData.value = value ?: 0.0
        }
    }

    private fun loadDefaultGoalHours() {
        for (category in Constants.CATEGORIES) {
            val defaultValue = defaultGoalHours[category] ?: 10.0
            val savedValue =
                sharedPreferences.getFloat(defaultGoalHoursKey + category, defaultValue.toFloat())
            _goalHoursMap[category] = MutableLiveData(savedValue.toDouble())
        }
    }

    private fun saveDefaultGoalHours() {
        val editor = sharedPreferences.edit()
        for (category in Constants.CATEGORIES) {
            _goalHoursMap[category]?.value?.toFloat()?.let {
                editor.putFloat(defaultGoalHoursKey + category, it)
            }
        }
        editor.apply()
    }

    fun updateGoalHours(category: String, newValue: Double) {
        _goalHoursMap[category]?.value = newValue
        saveDefaultGoalHours()
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

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}