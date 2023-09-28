package com.ashwinmadhavan.codecadence.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ashwinmadhavan.codecadence.data.StudyItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(navigate: (String) -> Unit) {
//    val taskListViewModel: ListDataManager = viewModel()
//    val viewModelTasks = taskListViewModel.readLists().toList() // immutable collection of items
//    var tasks by remember { mutableStateOf(viewModelTasks) }
    // tasks is a state variable, Jetpack Compose needs to be explicitly notified of changes in order to trigger an event
    // which is possible with mutableStateOf()
    // the only reason you do it in two lines is so that it doesn't look cluttered like this: var tasks by remember { mutableStateOf(taskListViewModel.readLists().toList()) }

    val studyItems = listOf(
        StudyItem("Array", 10, 9),
        StudyItem("Linked List", 15, 8),
        StudyItem("Stack", 8, 2),
        StudyItem("Queue", 12, 1),
        StudyItem("Binary Tree", 20, 15),
        StudyItem("Hashing", 18, 12),
        StudyItem("Graph", 25, 20),
    )


    Scaffold(
        topBar = {

        },
        content = {
            categoryListContent(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                categories = studyItems,
                onClick = { }
            )
        },
        floatingActionButton = {
        }
    )
}


@Composable
fun categoryListContent(
    modifier: Modifier,
    categories: List<StudyItem>,
    onClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(categories) {
                ListItemView(
                    studyItem = it,
                    onClick = onClick
                )
            }
        }
    )
}
