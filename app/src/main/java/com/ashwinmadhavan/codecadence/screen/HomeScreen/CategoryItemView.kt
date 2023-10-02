package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryItemView(maxGoalHrs: Double, categoryItem: CategoryItem, onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onClick() },
        shape = shape,
        color = Color.LightGray.copy(alpha = 0.3f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Study item name
            Text(
                text = categoryItem.name,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(end = 16.dp)
            )

            // Progress bar
            val progress = (categoryItem.completedHours / categoryItem.goalHours).toFloat()
            val barLen = (categoryItem.goalHours / maxGoalHrs).toFloat()
            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(barLen)
                        .background(Color.Cyan),
                    progress = progress,
                )
            }
        }
    }
}

