package com.ashwinmadhavan.codecadence.screen.HomeScreen

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
    val totalHours by viewModel.totalHours.observeAsState(initial = 0.0)
    var showDialog by remember { mutableStateOf(false) }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        onPressButton()

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


        CustomDoubleDisplay(double1 = totalHours, double2 = 200.00)
        Text(text = "total hrs")
        Spacer(modifier = Modifier.height(16.dp))
        categoryItemList(viewModel = viewModel, onItemClick = onItemClick)

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Set Goal Hours")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("Set")
                    }
                },
                text = {
                    editableCategoryItemList(viewModel = viewModel)
                }
            )
        }
    }
}

@Composable
fun onPressButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val interactionSource2 = remember { MutableInteractionSource() }
    val isPressed2 by interactionSource2.collectIsPressedAsState()

    var currentStateTxt by remember { mutableStateOf("Not Pressed") }
    var currentStateTxt2 by remember { mutableStateOf("Not Pressed") }
    var currentCount by remember { mutableStateOf(0) }

    if (isPressed) {
        //Pressed
        currentStateTxt = "Pressed"
        currentCount += 1

        //Use if + DisposableEffect to wait for the press action is completed
        DisposableEffect(Unit) {
            onDispose {
                //released
                currentStateTxt = "Released"
            }
        }
    }

    if (isPressed2) {
        //Pressed
        currentStateTxt2 = "Pressed"
        if (currentCount > 0) {
            currentCount -= 1
        } else {
            currentCount = 0
        }

        //Use if + DisposableEffect to wait for the press action is completed
        DisposableEffect(Unit) {
            onDispose {
                //released
                currentStateTxt2 = "Released"
            }
        }
    }

    Button(
        onClick = {},
        interactionSource = interactionSource
    ) {
        Text("Current state = $currentStateTxt")
    }
    Button(
        onClick = {},
        interactionSource = interactionSource2
    ) {
        Text("Current state2 = $currentStateTxt2")
    }
    Text("Count = $currentCount")
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

        Log.d("CustomDD double1 val: ", double1.toString())
        var progress by remember { mutableStateOf(double1 / double2) }
        Log.d("CustomDD progress val: ", progress.toString())

        Column {
            CircularProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier.padding(10.dp),
                color = Color.Green
            )
        }

        Log.d("CustomDD", "Checkpoint")
    }
}

@Composable
fun editableCategoryItemList(viewModel: HomeViewModel) {
    val list = Constants.CATEGORIES.map { category ->
        val goalHoursForCategory = viewModel.getGoalHours(category).value ?: 0.0
        val totalHoursForCategory = viewModel.totalHoursMap[category]?.value ?: 0.0
        CategoryItem(category, goalHoursForCategory, totalHoursForCategory)
    }

    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 5.dp)
        ) {
            items(list) { item ->
                CategoryItemRow(item, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItemRow(categoryItem: CategoryItem, viewModel: HomeViewModel) {
    var goalHours by remember { mutableStateOf(categoryItem.goalHours.toString()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text taking half the row
        Text(
            text = categoryItem.name,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )

        // Decrement button
        IconButton(onClick = {
            val updatedGoalHours = (goalHours.toDoubleOrNull() ?: 0.0) - 1.0
            goalHours = updatedGoalHours.toString()
            viewModel.setGoalHours(categoryItem.name, updatedGoalHours)
        }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Decrement")
        }

        // Goal Hours display
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = goalHours,
            modifier = Modifier.width(50.dp)
        )

        // Increment button
        IconButton(onClick = {
            val updatedGoalHours = (goalHours.toDoubleOrNull() ?: 0.0) + 1.0
            goalHours = updatedGoalHours.toString()
            viewModel.setGoalHours(categoryItem.name, updatedGoalHours)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Increment")
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