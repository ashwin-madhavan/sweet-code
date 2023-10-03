package com.ashwinmadhavan.codecadence.screen.LogsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.Constants
import com.ashwinmadhavan.codecadence.data.LogEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {
    val viewModel: LogsViewModel = viewModel()
    val logs: List<LogEntity> by viewModel.allLogs.observeAsState(emptyList())

    Scaffold(
        content = {
            Column {
                Button(onClick = { viewModel.deleteAllLogs() }) {
                    Text("Delete All Logs")
                }
                if (logs != null && logs.isNotEmpty()) {
                    TableScreen(logs)
                } else {
                    Text(text = "Loading...")
                }
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

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun TableScreen(logs: List<LogEntity>) {
    // Each cell of a column must have the same weight.
    val column1Weight = .45f // 30%
    val column2Weight = .35f // 70%
    val column3Weight = .2f // 30%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Here is the header
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Date", weight = column1Weight)
                TableCell(text = "Category", weight = column2Weight)
                TableCell(text = "Hrs", weight = column3Weight)
            }
        }
        // Here are all the lines of your table.
        items(logs) { log ->
            Row(Modifier.fillMaxWidth()) {
                val date = Date() // Replace this with your Date object
                val formattedDate = formatDate(date)
                TableCell(text = formattedDate, weight = column1Weight)
                TableCell(text = log.category, weight = column2Weight)
                TableCell(text = log.totalHours.toString(), weight = column3Weight)
            }
        }
    }
}

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("MM/dd/yy h:mm a", Locale.getDefault())
    return dateFormat.format(date)
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

        var expanded by remember { mutableStateOf(false) }
        var selectedCategory by remember { mutableStateOf(categories[0]) }
        var date by remember { mutableStateOf(Date()) }
        var totalHours by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = title) },
            text = {
                Column {
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
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    TextField(
                        value = totalHours,
                        onValueChange = { totalHours = it },
                        label = { Text("Total Hours") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
                        totalHours = totalHours.toDoubleOrNull() ?: 0.0,
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