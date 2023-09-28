package com.ashwinmadhavan.codecadence.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListItemView(value: String, onClick: (String) -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    val clickedItem by remember { mutableStateOf(value) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick(clickedItem) },
        shape = shape,
        color = Color.LightGray.copy(alpha = 0.3f),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .padding(4.dp)
            )
            Spacer(Modifier.size(8.dp))
        }

    }
}

@Preview
@Composable
fun ListItemViewPreview() {
    ListItemView("test", {})
}

