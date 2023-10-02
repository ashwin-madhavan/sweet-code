package com.ashwinmadhavan.codecadence.screen.HomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryItemView(studyItem: CategoryItem, onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    val normalizedFraction =
        (studyItem.completedHours.toFloat() / studyItem.goalHours.toFloat()).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onClick() },
        shape = shape,
        color = Color.LightGray.copy(alpha = 0.3f),
    ) {

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = studyItem.name,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f / 4)
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .weight(3f / 4),
                progress = normalizedFraction,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}