package com.ashwinmadhavan.codecadence.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.data.LogEntity
import com.ashwinmadhavan.codecadence.data.User
import java.util.Date

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
                //UserDatabaseBox(viewModel)
                //LogTableBox(viewModel)
                if (logs != null) {
                    if (logs.isNotEmpty()) {
                        TableScreen(logs)
                    }
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
    val column1Weight = .3f // 30%
    val column2Weight = .4f // 70%
    val column3Weight = .3f // 30%
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
                TableCell(text = log.id.toString(), weight = column1Weight)
                TableCell(text = log.category, weight = column2Weight)
                TableCell(text = log.totalHours.toString(), weight = column3Weight)
            }
        }
    }
}


@Composable
fun LogItem(log: LogEntity) {
    // Display each property in a column
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Log ID: ${log.id}")
            Text("Category: ${log.category}")
            Text("Date: ${log.date}")
        }
        Column {
            Text("Start Time: ${log.startTime}")
            Text("End Time: ${log.endTime}")
            Text("Total Hours: ${log.totalHours}")
        }
        Column {
            Text("Notes: ${log.notes}")
        }
    }
}

@Composable
fun LogList(logs: List<LogEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (logs.isNotEmpty()) {
            item {
                // Display column titles
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(8.dp)
                ) {
                    Text("Log ID", fontWeight = FontWeight.Bold)
                    Text("Category", fontWeight = FontWeight.Bold)
                    Text("Date", fontWeight = FontWeight.Bold)
                    Text("Start Time", fontWeight = FontWeight.Bold)
                    Text("End Time", fontWeight = FontWeight.Bold)
                    Text("Total Hours", fontWeight = FontWeight.Bold)
                    Text("Notes", fontWeight = FontWeight.Bold)
                }
            }

            // Display log items
            items(logs) { log ->
                LogItem(log = log)
            }
        } else {
            item {
                Text(text = "No Logs available.")
            }
        }
    }
}

@Composable
fun LogTableBox(viewModel: LogsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .border(2.dp, Color.Cyan)
    ) {
        Column {
            Button(onClick = {
                viewModel.insertLog(
                    category = "Work",
                    date = Date(),
                    startTime = "09:00 AM",
                    endTime = "05:00 PM",
                    totalHours = 8.0,
                    notes = "Completed project X."
                )
            }) {
                Text("Insert Log")
            }





            Button(onClick = { viewModel.deleteAllLogs() }) {
                Text("Delete All Logs")
            }

            val logs: List<LogEntity> by viewModel.allLogs.observeAsState(emptyList())

            if (logs != null) {
                LogList(logs)
            } else {
                Text(text = "Loading...")
            }
        }
    }
}

@Composable
fun UserDatabaseBox(viewModel: LogsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .border(2.dp, Color.Cyan)
    ) {
        Column {
            Button(onClick = {
                viewModel.insertUser("John", "Doe")
            }) {
                Text("Insert User")
            }

            Button(onClick = { viewModel.deleteAllUsers() }) {
                Text("Delete All Users")
            }

            // Observe the allUsers LiveData
            val users: List<User> by viewModel.allUsers.observeAsState(emptyList())

            if (users != null) {
                if (users.isNotEmpty()) {
                    users.forEach { user ->
                        Text(text = "User ID: ${user.uid}, Name: ${user.firstName} ${user.lastName}")
                    }
                } else {
                    Text(text = "No users available.")
                }
            } else {
                Text(text = "Loading...")
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
    var taskName by remember { mutableStateOf("") }

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
        val categories = listOf("Work", "Personal", "Study", "Other")

        var expanded by remember { mutableStateOf(false) }
        var selectedCategory by remember { mutableStateOf(categories[0]) }
        var date by remember { mutableStateOf(Date()) }
        var startTime by remember { mutableStateOf("") }
        var endTime by remember { mutableStateOf("") }
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
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") }
                    )

                    TextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") }
                    )

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
                        startTime = startTime,
                        endTime = endTime,
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