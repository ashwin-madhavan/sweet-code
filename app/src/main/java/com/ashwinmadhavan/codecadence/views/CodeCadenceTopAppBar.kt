package com.ashwinmadhavan.codecadence.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeCadenceTopAppBar(titles: List<Pair<String, () -> Unit>>) {
    var selectedTitleIndex by remember { mutableStateOf(0) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                titles.forEachIndexed { index, (title, onClick) ->
                    val isSelected = index == selectedTitleIndex
                    Text(
                        text = title,
                        modifier = Modifier
                            .clickable {
                                selectedTitleIndex = index
                                onClick()
                            }
                            .padding(16.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.inversePrimary
                                else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

