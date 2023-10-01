package com.ashwinmadhavan.codecadence.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.data.LogEntity
import com.ashwinmadhavan.codecadence.data.User
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {
    val viewModel: TestViewModel = viewModel()

    Scaffold(
        content = {
            Column {
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

                LogTableBox(viewModel)
            }
        },
        floatingActionButton = {
            ListMakerFloatingActionButton(
                title = "title",
                inputHint = "hint",
                onFabClick = {

                }
            )
        }
    )
}

@Composable
fun LogItem(log: LogEntity) {
    Text(
        text = "Log ID: ${log.id}, " +
                "Category: ${log.category}, " +
                "Date: ${log.date}, " +
                "Start Time: ${log.startTime}, " +
                "End Time: ${log.endTime}, " +
                "Total Hours: ${log.totalHours}, " +
                "Notes: ${log.notes}"
    )
}

@Composable
fun LogList(logs: List<LogEntity>) {
    LazyColumn {
        if (logs.isNotEmpty()) {
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
fun LogTableBox(viewModel: TestViewModel) {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMakerFloatingActionButton(
    title: String,
    inputHint: String,
    onFabClick: (String) -> Unit
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
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = title) },
            text = {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text(text = inputHint) },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onFabClick(taskName)
                        taskName = ""
                    },
                    content = {
                        Text(text = "Create Label")
                    }
                )
            },
        )
    }
}