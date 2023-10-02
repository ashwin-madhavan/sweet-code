package com.ashwinmadhavan.codecadence.screen.HomeScreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val name: String,
    val goalHours: Double,
    val completedHours: Double,
) : Parcelable