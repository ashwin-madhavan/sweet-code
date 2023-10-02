package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashwinmadhavan.codecadence.MyApplication

class HomeViewModel : ViewModel() {
    private val logDao = MyApplication.logDatabase.logDao()

    private val _totalHours = MutableLiveData<Double>()
    val totalHours: LiveData<Double> get() = _totalHours

    init {
        logDao.getTotalHoursForCategoryLiveData("Work").observeForever {
            _totalHours.postValue(it)
        }
    }
}