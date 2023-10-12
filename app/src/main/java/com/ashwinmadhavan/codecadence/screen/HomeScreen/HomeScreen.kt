package com.ashwinmadhavan.codecadence.screen.HomeScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onItemClick: () -> Unit) {
    val viewModel: HomeViewModel =
        viewModel(factory = HomeViewModelFactory(context = LocalContext.current))
    val totalHours by viewModel.totalHours.observeAsState(initial = 0.0)
    var showDialog by remember { mutableStateOf(false) }

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


        CustomDoubleDisplay(double1 = totalHours, double2 = viewModel.totalGoalHours)
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CategoryItemRow(categoryItem: CategoryItem, viewModel: HomeViewModel) {
    val plusButtonInteractionSource = remember { MutableInteractionSource() }
    val isPlusButtonPressed by plusButtonInteractionSource.collectIsPressedAsState()

    val minusButtonInteractionSource = remember { MutableInteractionSource() }
    val isMinusButtonPressed by minusButtonInteractionSource.collectIsPressedAsState()

    var currentCount by remember { mutableStateOf(categoryItem.goalHours) }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val plusJob = coroutineScope.launch {
            while (true) {
                if (isPlusButtonPressed) {
                    currentCount += 1
                }
                delay(75)
            }
        }

        val minusJob = coroutineScope.launch {
            while (true) {
                if (isMinusButtonPressed && currentCount > 0) {
                    currentCount -= 1
                }
                delay(75)
            }
        }

        onDispose {
            plusJob.cancel()
            minusJob.cancel()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = categoryItem.name,
            modifier = Modifier
                .weight(0.6f)
                .padding(end = 8.dp),
            fontSize = 16.sp
        )

        IconButton(
            onClick = {
                if (currentCount > 0) {
                    currentCount -= 1
                }
                viewModel.updateGoalHours(categoryItem.name, currentCount)
            },
            interactionSource = minusButtonInteractionSource,
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Decrement",
                tint = Color.White
            )
        }

        Text(
            text = "${currentCount.toInt()}",
            fontSize = 16.sp,
            modifier = Modifier
                .weight(0.2f)
                .wrapContentSize(Alignment.Center)
        )

        IconButton(
            onClick = {
                currentCount += 1
                viewModel.updateGoalHours(categoryItem.name, currentCount)
            },
            interactionSource = plusButtonInteractionSource,
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increment",
                tint = Color.White
            )
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
        val shape = RoundedCornerShape(8.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = shape,
            color = Color.LightGray.copy(alpha = 0.7f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.25f))
                Text(text = "0", modifier = Modifier.weight(0.375f), textAlign = TextAlign.Start)
                Text(
                    text = "$maxGoalHours",
                    modifier = Modifier.weight(0.375f),
                    textAlign = TextAlign.End
                )
            }
        }

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