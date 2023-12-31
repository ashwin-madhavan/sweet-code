package com.ashwinmadhavan.codecadence.screen.HomeScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashwinmadhavan.codecadence.BuildConfig
import com.ashwinmadhavan.codecadence.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onItemClick: () -> Unit) {
    val viewModel: HomeViewModel =
        viewModel(factory = HomeViewModelFactory(context = LocalContext.current))
    val totalHours by viewModel.totalHours.observeAsState(initial = 0.0)
    var showDialog by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // TODO: Not sure why the line below is needed to populate the progress bars
        viewModel.totalHoursMap.forEach { (category, totalHoursLiveData) ->
            val totalHours: Double? by totalHoursLiveData.observeAsState(null)

            when {
                totalHours != null -> {
                    //Text(text = "Total $category Hours: $totalHours")
                }

                else -> {
                    // Text(text = "Not Started")
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Overview", style = TextStyle(
                fontSize = 24.sp,  // Adjust the font size as needed
                fontWeight = FontWeight.Bold,
                color = Color.Black  // Adjust the color as needed
            )
        )
        Spacer(modifier = Modifier.height(16.dp))


        CustomDoubleDisplay(double1 = totalHours, double2 = viewModel.totalGoalHours)
        Text(text = "total hrs")
        Spacer(modifier = Modifier.height(16.dp))
        categoryItemList(viewModel = viewModel, onItemClick = onItemClick)


        Spacer(modifier = Modifier.height(25.dp))
        Row() {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text("Adjust Target Hours")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("Set")
                    }
                },
                text = {
                    var aiResponse by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        editableCategoryItemList(viewModel = viewModel)
                        var userInput by remember { mutableStateOf("") }

                        TextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            placeholder = { Text("Enter you goals, timeline, etc here to generate a plan!") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(bottom = 5.dp)
                        )
                        Button(
                            onClick = {
                                val responseFuture = getResponse(userInput)
                                aiResponse = responseFuture.join()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Generate AI Suggestion")
                        }
                        ScrollableText(text = "AI response: $aiResponse")
                    }
                }
            )
        }
    }
}


@Composable
fun ScrollableText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(2.dp)
            .border(1.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Text(text = text)
        }
    }
}


@Composable
fun CustomDoubleDisplay(double1: Double, double2: Double) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%.2f", double1),
            style = MaterialTheme.typography.h6.copy(fontSize = 24.sp)
        )
        Text(
            text = "/",
            style = MaterialTheme.typography.h6.copy(fontSize = 24.sp)
        )
        Text(
            text = String.format("%.2f", double2),
            style = MaterialTheme.typography.h6.copy(fontSize = 24.sp)
        )

        var progress by remember { mutableStateOf(double1 / double2) }

        Column {
            CircularProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier.padding(10.dp),
                color = Color.Green
            )
        }
    }
}

@Composable
fun editableCategoryItemList(viewModel: HomeViewModel) {
    val list = Constants.CATEGORIES.map { category ->
        val goalHoursForCategory = viewModel.getGoalHours(category).value ?: 0.0
        val totalHoursForCategory = viewModel.totalHoursMap[category]?.value ?: 0.0
        CategoryItem(category, goalHoursForCategory, totalHoursForCategory)
    }

    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 5.dp)
        ) {
            items(list) { item ->
                CategoryItemRow(item, viewModel)
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CategoryItemRow(categoryItem: CategoryItem, viewModel: HomeViewModel) {
    val plusButtonInteractionSource = remember { MutableInteractionSource() }
    val isPlusButtonPressed by plusButtonInteractionSource.collectIsPressedAsState()

    val minusButtonInteractionSource = remember { MutableInteractionSource() }
    val isMinusButtonPressed by minusButtonInteractionSource.collectIsPressedAsState()

    var currentCount by remember { mutableStateOf(categoryItem.goalHours) }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val plusJob = coroutineScope.launch {
            while (true) {
                if (isPlusButtonPressed) {
                    currentCount += 1
                }
                delay(75)
            }
        }

        val minusJob = coroutineScope.launch {
            while (true) {
                if (isMinusButtonPressed && currentCount > 0) {
                    currentCount -= 1
                }
                delay(75)
            }
        }

        onDispose {
            plusJob.cancel()
            minusJob.cancel()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = categoryItem.name,
            modifier = Modifier
                .weight(0.6f)
                .padding(end = 8.dp),
            fontSize = 16.sp
        )

        IconButton(
            onClick = {
                if (currentCount > 0) {
                    currentCount -= 1
                }
                viewModel.updateGoalHours(categoryItem.name, currentCount)
            },
            interactionSource = minusButtonInteractionSource,
            modifier = Modifier
                .size(20.dp)
                .background(Color.Red)
        ) {
            Text(
                text = "-",
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "${currentCount.toInt()}",
            fontSize = 16.sp,
            modifier = Modifier
                .weight(0.2f)
                .wrapContentSize(Alignment.Center)
        )

        IconButton(
            onClick = {
                currentCount += 1
                viewModel.updateGoalHours(categoryItem.name, currentCount)
            },
            interactionSource = plusButtonInteractionSource,
            modifier = Modifier
                .size(20.dp)
                .background(Color.Green)
        ) {
            Text(
                text = "+",
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun categoryItemList(viewModel: HomeViewModel, onItemClick: () -> Unit) {
    val list = Constants.CATEGORIES.map { category ->
        val goalHoursForCategory = viewModel.getGoalHours(category).value ?: 0.0
        val totalHoursForCategory = viewModel.totalHoursMap[category]?.value ?: 0.0
        CategoryItem(category, goalHoursForCategory, totalHoursForCategory)
    }

    val maxGoalHours = list.maxByOrNull { it.goalHours }?.goalHours ?: 0.0
    Column {
        val shape = RoundedCornerShape(8.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = shape,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.25f))
                Text(text = "0", modifier = Modifier.weight(0.375f), textAlign = TextAlign.Start)
                Text(
                    text = "$maxGoalHours",
                    modifier = Modifier.weight(0.375f),
                    textAlign = TextAlign.End
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 5.dp)
        ) {
            items(list) { item ->
                CategoryItemView(maxGoalHours, categoryItem = item, onClick = { })
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun getResponse(context: String): CompletableFuture<String> {
    val completableFuture = CompletableFuture<String>()

    val client = OkHttpClient()

    val apiKey = BuildConfig.api_key
    val url = "https://api.openai.com/v1/engines/text-davinci-003/completions"
    val requestBody = """
            {
            "prompt": "I have to study for a CS interview. $context Can you give me on how many hours I should study for the following categories:\nArrays, Linked List, Stack, Queue, Binary Tree, Hashing, and Graph.\n\nGive the answer comma separated in the following format:\nArray: a\nLinkedList: b\nStack: c\nQueue: d\nBinary Tree: e\nHashing: f\nGraph: g\n\nThen followed by a three sentence explanation of why those numbers were chosen.",
            "max_tokens": 800,
            "temperature": 0.7
            }
        """.trimIndent()

    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            completableFuture.completeExceptionally(e)
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val body = response.body?.string()
                if (body != null) {
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult = jsonArray.getJSONObject(0).getString("text")
                    completableFuture.complete(textResult)
                } else {
                    completableFuture.complete("Empty response")
                }
            } catch (e: Exception) {
                completableFuture.completeExceptionally(e)
            }
        }
    })

    return completableFuture
}