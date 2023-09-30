package com.ashwinmadhavan.codecadence.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.data.User

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {
    val viewModel: TestViewModel = viewModel()

    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
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
                        Text(text= "Loading...")
                    }
                }
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