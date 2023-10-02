package com.github.af2905.jetpack_compose_navigation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.ashwinmadhavan.codecadence.screen.CategoryItem
import com.ashwinmadhavan.codecadence.screen.CategoryItemView

@Composable
fun HomeScreen(onItemClick: () -> Unit) {
    categoryItemList(onItemClick = onItemClick)
}

@Composable
fun categoryItemList(onItemClick: () -> Unit) {
    val list = mutableListOf(
        CategoryItem("Array", 10, 9),
        CategoryItem("Linked List", 15, 8),
        CategoryItem("Stack", 8, 2),
        CategoryItem("Queue", 12, 1),
        CategoryItem("Binary Tree", 20, 15),
        CategoryItem("Hashing", 18, 12),
        CategoryItem("Graph", 25, 20)
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(all = 3.dp)
    ) {
        items(list) { item ->
            CategoryItemView(studyItem = item, onClick = onItemClick)
        }
    }
}