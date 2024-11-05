package ai.soundcast.offlinegpt.View.Chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ai.soundcast.offlinegpt.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptLibraryScreen(navController: NavController) {
    val promptTitles = listOf(
        "Explain this code",
        "Debug this error",
        "Review my business idea",
        "Help me write an essay",
        "Explain it like five",
        "Suggest books on",
        "Suggest movies"
    )

    val promptMap = mapOf(
        "Explain this code" to "you are a professional software developer so Explain this code to me",
        "Debug this error" to "You are professional software developer so debug this code for me",
        "Review my business idea" to "You are a smart businessman review my business idea",
        "Help me write an essay" to "You are a professional copy writer help me write an essay on",
        "Explain it like five" to "Explain complex ideas in simple terms.",
        "Suggest books on" to "Suggest some books on a specified topic.",
        "Suggest movies" to "Suggest some interesting movies."
    )
    // State variables
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val filteredPrompts = remember(searchQuery) {
        promptTitles.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF040605),
                        titleContentColor = Color.White,
                    ),
                    title = {
                        if (isSearching) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Search...") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Color.White,
                                    placeholderColor = Color.Gray,
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.Gray
                                )
                            )
                        } else {
                            Text("Prompt Library", fontSize = 24.sp, fontWeight = FontWeight.Normal)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                isSearching = !isSearching // Toggle search bar visibility
                                if (!isSearching) searchQuery = "" // Clear search when closing
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.outline_search),
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            containerColor = Color(0xFF040605)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(bottom = 0.dp)
                    .fillMaxSize()
                    .background(color = Color(0xFF040605)),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.Top
            ) {
                items(filteredPrompts.size) { index ->
                    val promptTitle = filteredPrompts[index]
                    val promptText = promptMap[promptTitle] ?: ""
                    PromptItem(promptTitle, promptText, navController)
                }
            }
        }
    }
}

@Composable
fun PromptItem(promptTitle: String, promptText: String, navController: NavController) {




    Row(
        modifier = Modifier
            .height(60.dp)
            .padding(end = 14.dp)
            .fillMaxWidth()
            .background(color = Color(0xFF161719)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = promptTitle,
            fontSize = 19.sp,
            color = Color(0xFFf5f5f5),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        )
        UseButton(onUseClick = {
            navController.navigate("chat?promptText=${promptText}")
        })
    }
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun UseButton(onUseClick: () -> Unit) {
    IconButton(
        onClick = onUseClick,
        modifier = Modifier
            .size(20.dp)
            .background(color = Color(0xFFFFFFFF), shape = CircleShape)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
            contentDescription = "Use",
            tint = Color(0xFF000000),
            modifier = Modifier.size(20.dp)
        )
    }
}
