package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
        color = Color.LightGray.copy(alpha = 0.7f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryItem.name,
                modifier = Modifier
                    .weight(0.25f)
                    .padding(end = 4.dp)
            )

            val progress = (categoryItem.completedHours / categoryItem.goalHours).toFloat()
            val barLen = (categoryItem.goalHours / maxGoalHrs).toFloat()

            val color = when {
                progress <= 0.25f -> Color.Red
                progress <= 0.5f -> Color(1.0f, 0.5f, 0.0f, 1.0f)
                progress <= 0.75f -> Color.Yellow
                else -> Color.Green
            }

            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(barLen)
                        .height(16.dp),
                    color = color, // Set color dynamically based on progress
                    progress = progress,
                )
            }

        }
    }
}

