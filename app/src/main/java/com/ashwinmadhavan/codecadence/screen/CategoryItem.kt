package com.ashwinmadhavan.codecadence.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val name: String,
    val goalHours: Int,
    val completedHours: Int,
) : Parcelable