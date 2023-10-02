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
import com.ashwinmadhavan.codecadence.Constants

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

        categoryItemList(viewModel = viewModel, onItemClick = onItemClick)
    }
}


@Composable
fun categoryItemList(viewModel: HomeViewModel, onItemClick: () -> Unit) {
    val list = Constants.CATEGORIES.map { category ->
        val goalHoursForCategory = viewModel.getGoalHours(category).value ?: 0.0
        val totalHoursForCategory = viewModel.totalHoursMap[category]?.value ?: 0.0
        CategoryItem(category, goalHoursForCategory, totalHoursForCategory)
    }
    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 3.dp)
        ) {
            items(list) { item ->
                CategoryItemView(20.0, categoryItem = item, onClick = onItemClick)
            }
        }
    }
}