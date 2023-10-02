package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(onItemClick: () -> Unit) {
    val viewModel: HomeViewModel = viewModel()

    Column {
        viewModel.totalHoursMap.forEach { (category, totalHoursLiveData) ->
            val totalHours: Double? by totalHoursLiveData.observeAsState(null)

            when {
                totalHours != null -> {
                    Text(text = "Total $category Hours: $totalHours")
                }
                else -> {
                    Text(text = "Not Started")
                }
            }
        }

        categoryItemList(onItemClick = onItemClick)
    }
}


@Composable
fun categoryItemList(onItemClick: () -> Unit) {
    val list = mutableListOf(
        CategoryItem("Array", 10, 9),
        CategoryItem("Linked List", 15, 8),
        CategoryItem("Stack", 8, 2),
        CategoryItem("Queue", 12, 1),
        CategoryItem("Binary Tree", 20, 15),
        CategoryItem("Hashing", 18, 12),
        CategoryItem("Graph", 25, 20)
    )
    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 3.dp)
        ) {
            items(list) { item ->
                CategoryItemView(studyItem = item, onClick = onItemClick)
            }
        }
    }
}