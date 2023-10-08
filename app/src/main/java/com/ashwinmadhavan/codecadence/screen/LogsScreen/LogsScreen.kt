package com.ashwinmadhavan.codecadence.screen.LogsScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.Constants
import com.ashwinmadhavan.codecadence.data.LogEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {
    val viewModel: LogsViewModel = viewModel()
    val logs: List<LogEntity> by viewModel.allLogs.observeAsState(emptyList())
    //val stopWatch = remember { Domain() }

    val categories = Constants.CATEGORIES

    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var date by remember { mutableStateOf("") }
    var totalHours by remember { mutableStateOf(0.0) }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        content = {
            Column {
                Row {
                    /**                   Box(
                    modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(Color(0xFF212121)),
                    contentAlignment = Alignment.Center,
                    ) {
                    Presentation(
                    formattedTime = stopWatch.formattedTime,
                    onStartClick = stopWatch::start,
                    onPauseClick = stopWatch::pause,
                    onResetClick = stopWatch::reset
                    )
                    }
                     **/

                }

                Button(onClick = { viewModel.deleteAllLogs() }) {
                    Text("Delete All Logs")
                }
                if (logs != null && logs.isNotEmpty()) {
                    TableScreen(logs, viewModel = viewModel)
                } else {
                    Text(text = "No logs to display yet! Click the + to get started.")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    title = {
                        Text(text = "Submit Log")
                    },
                    text = {
                        /**                        val formattedTimeString = stopWatch.formattedTime

                        totalHours = formattedTimeString.split(":")
                        .mapIndexed { index, value ->
                        value.toDouble() / (60.0.pow(index.toDouble()))
                        }
                        .sum()
                         **/
                        Column {
                            Text(text = "Total Hours: %.2f".format(totalHours))
                            Spacer(modifier = Modifier.height(16.dp))
                            Slider(
                                value = totalHours.toFloat(),
                                onValueChange = { newTotalHours ->
                                    totalHours = newTotalHours.toDouble()
                                },
                                valueRange = 0.0f..3.0f, // Adjust the range based on your requirements
                                steps = 0,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                            )

                            val dateObject = createDateFromTotalHours(totalHours)
                            date = formatDateString(dateObject)

                            Text(
                                text = "Started At: $date"
                            )

                            DropdownCategorySelector(
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { newCategory ->
                                    selectedCategory = newCategory
                                }
                            )

                            TextField(
                                value = notes,
                                onValueChange = { notes = it },
                                label = { Text("Notes") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.insertLog(
                                    category = selectedCategory,
                                    date = date,
                                    totalHours = totalHours,
                                    notes = notes
                                )
                                showDialog = false
                            }
                        ) {
                            Text(text = "OK")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            LogMakerFloatingActionButton(
                title = "Add Entry",
                viewModel = viewModel
            )
        }
    )
}

fun createDateFromTotalHours(totalHours: Double): Date {
    val currentDateTime = Date()
    val millisecondsInHour = 3600000 // 1 hour in milliseconds
    val timeToSubtract = (totalHours * millisecondsInHour).toLong()
    val newDateTime = Date(currentDateTime.time - timeToSubtract)
    return newDateTime
}

fun formatDateString(date: Date): String {
    val pattern = "MM/dd/yy h:mm a"
    val dateFormat = SimpleDateFormat(pattern, Locale.US)
    return dateFormat.format(date)
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.White) // Set border color to white
            .weight(weight)
            .padding(8.dp)
            .height(22.dp)
    )
}

@Composable
fun RowScope.TableCellBtn(
    weight: Float,
    onLogDelete: () -> Unit
) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = "Delete",
        modifier = Modifier
            .clickable { onLogDelete() }
            .border(1.dp, Color.White)
            .weight(weight)
            .padding(8.dp)
            .height(22.dp)
    )
}


@Composable
fun TableScreen(logs: List<LogEntity>, viewModel: LogsViewModel) {
    val column1Weight = .45f
    val column2Weight = .3f
    val column3Weight = .15f
    val deleteColumnWeight = .1f

    val reversedLogs = logs.reversed()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
            ) {
                TableCell(text = "Date", weight = column1Weight)
                TableCell(text = "Category", weight = column2Weight)
                TableCell(text = "Hrs", weight = column3Weight)
                TableCell(text = "", weight = deleteColumnWeight)
            }
        }

        items(reversedLogs) { log ->
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = log.date, weight = column1Weight)
                TableCell(text = log.category, weight = column2Weight)
                TableCell(text = String.format("%.2f", log.totalHours), weight = column3Weight)
                TableCellBtn(
                    weight = deleteColumnWeight,
                    onLogDelete = { viewModel.deleteLogByID(log.id.toLong()) }
                )
            }
        }
    }
}


@Composable
fun DropdownCategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(Color.Gray)
                .padding(16.dp)
        ) {
            Text(text = "Select Category: $selectedCategory")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category)
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMakerFloatingActionButton(
    title: String,
    viewModel: LogsViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            showDialog = true
        },
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Label"
            )
        }
    )

    if (showDialog) {
        val categories = Constants.CATEGORIES

        var selectedCategory by remember { mutableStateOf(categories[0]) }
        var date by remember { mutableStateOf("") }
        var totalHours by remember { mutableStateOf(0.0) }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = title) },
            text = {
                Column {
                    DisplayTotalHours(totalHours = totalHours)
                    Spacer(modifier = Modifier.height(16.dp))
                    Slider(
                        value = totalHours.toFloat(),
                        onValueChange = { newTotalHours ->
                            totalHours = newTotalHours.toDouble()
                        },
                        valueRange = 0.0f..2.5f,
                        steps = 0,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )

                    val dateObject = createDateFromTotalHours(totalHours)
                    date = formatDateString(dateObject)

                    Text(
                        text = "Started At: $date"
                    )

                    DropdownCategorySelector(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { newCategory ->
                            selectedCategory = newCategory
                        }
                    )

                    TextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.insertLog(
                        category = selectedCategory,
                        date = date,
                        totalHours = totalHours,
                        notes = notes
                    )
                    showDialog = false
                }) {
                    Text("Insert Log")
                }
            }
        )
    }
}

@Composable
fun DisplayTotalHours(totalHours: Double) {
    val hours = totalHours.toInt()
    val minutes = ((totalHours - hours) * 60).toInt()

    val formattedTotalHours = if (hours > 0) {
        if (minutes > 0) {
            "$hours hours and $minutes mins"
        } else {
            "$hours hours"
        }
    } else {
        "$minutes mins"
    }

    Text(text = "Total Hours: $formattedTotalHours")
}
