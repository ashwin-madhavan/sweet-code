package com.ashwinmadhavan.codecadence.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudyItem(
    val name: String,
    val goalHours: Int,
    val completedHours: Int,
) : Parcelable