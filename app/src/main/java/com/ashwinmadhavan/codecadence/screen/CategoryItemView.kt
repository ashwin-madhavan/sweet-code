package com.ashwinmadhavan.codecadence.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashwinmadhavan.codecadence.data.CategoryItem


//@Composable
//fun CategoryItemViewTest(studyItem: CategoryItem, onClick: () -> Unit) {
//    Card(
//        shape = RoundedCornerShape(8.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onItemClick() }
//    ) {
//        androidx.compose.material.Text(
//            text = "Item ${item.id}",
//            textAlign = TextAlign.Start,
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}

@Composable
fun CategoryItemView(studyItem: CategoryItem, onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    val normalizedFraction =
        (studyItem.completedHours.toFloat() / studyItem.goalHours.toFloat()).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick() },
        shape = shape,
        color = Color.LightGray.copy(alpha = 0.3f),
    ) {

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = studyItem.name,
                modifier = Modifier
                    .padding(4.dp)
            )
            Spacer(Modifier.size(8.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                progress = normalizedFraction, // Set the progress to 0.5 for half-filled
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.size(8.dp))
        }
    }
}