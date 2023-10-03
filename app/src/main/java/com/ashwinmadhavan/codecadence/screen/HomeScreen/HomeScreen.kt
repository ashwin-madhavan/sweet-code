package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.Constants

@Composable
fun HomeScreen(onItemClick: () -> Unit) {
    val viewModel: HomeViewModel = viewModel()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // TODO: Not sure why the line below is needed to populate the progress bars
        viewModel.totalHoursMap.forEach { (category, totalHoursLiveData) ->
            val totalHours: Double? by totalHoursLiveData.observeAsState(null)

            when {
                totalHours != null -> {
                    //Text(text = "Total $category Hours: $totalHours")
                }

                else -> {
                    // Text(text = "Not Started")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Overview", style = TextStyle(
                fontSize = 24.sp,  // Adjust the font size as needed
                fontWeight = FontWeight.Bold,
                color = Color.Black  // Adjust the color as needed
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomDoubleDisplay(double1 = 143.02, double2 = 200.00)
        Text(text = "total hrs")
        Spacer(modifier = Modifier.height(16.dp))
        categoryItemList(viewModel = viewModel, onItemClick = onItemClick)
    }
}

@Composable
fun ProgressIndicator() {

}


@Composable
fun CustomDoubleDisplay(double1: Double, double2: Double) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%.2f", double1),
            style = MaterialTheme.typography.h6.copy(fontSize = 24.sp)
        )
        Text(
            text = "/",
            style = MaterialTheme.typography.h6.copy(fontSize = 24.sp)
        )
        Text(
            text = String.format("%.2f", double2),
            style = MaterialTheme.typography.h6.copy(fontSize = 24.sp)
        )

        var progress by remember { mutableStateOf(double1 / double2) }
        Column {
            CircularProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier.padding(10.dp),
                color = Color.Green
            )
        }
    }
}

@Composable
fun viewTotalHrs(viewModel: HomeViewModel) {
    viewModel.totalHoursMap.forEach { (category, totalHoursLiveData) ->
        val totalHours: Double? by totalHoursLiveData.observeAsState(null)

        when {
            totalHours != null -> {
                //Text(text = "Total $category Hours: $totalHours")
            }

            else -> {
                // Text(text = "Not Started")
            }
        }
    }
}

@Composable
fun categoryItemList(viewModel: HomeViewModel, onItemClick: () -> Unit) {
    val list = Constants.CATEGORIES.map { category ->
        val goalHoursForCategory = viewModel.getGoalHours(category).value ?: 0.0
        val totalHoursForCategory = viewModel.totalHoursMap[category]?.value ?: 0.0
        CategoryItem(category, goalHoursForCategory, totalHoursForCategory)
    }

    // Calculate the maximum goal hours
    val maxGoalHours = list.maxByOrNull { it.goalHours }?.goalHours ?: 0.0
    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 5.dp)
        ) {
            items(list) { item ->
                CategoryItemView(maxGoalHours, categoryItem = item, onClick = onItemClick)
            }
        }
    }
}