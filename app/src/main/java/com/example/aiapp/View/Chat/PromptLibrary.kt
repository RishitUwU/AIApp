package com.example.aiapp.View.Chat

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiapp.Database.Note
import com.example.aiapp.R


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptLibraryScreen(){

    var filteredPrompts by remember { mutableStateOf<List<Note>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }


    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Scaffold(topBar = { TopAppBar(colors = topAppBarColors(
            containerColor = Color(0xFF040605),
            titleContentColor = Color.White,

        ),
            title = {
                Text("Prompt Library", fontSize = 24.sp, fontWeight = FontWeight.Normal )
            },
            actions = {
                IconButton(onClick = { /* Handle search */ }) {
                    Icon(modifier = Modifier.size(24.dp), painter = painterResource(id = R.drawable.outline_search), contentDescription = "Search", tint = Color.White)
                }
            }) }) { paddingValues ->
            LazyColumn (modifier = Modifier.padding(paddingValues).padding(bottom = 0.dp).fillMaxSize() .background(color = Color(0xFF040605)), contentPadding = PaddingValues(0.dp), verticalArrangement = Arrangement.Top ){
                items(20) { // Use items() to create a list
                    PromptItem()
                }
            }

        }
    }
}


@Composable
fun PromptItem(){
    Row (modifier = Modifier.height(60.dp).padding(end = 14.dp).fillMaxWidth().background(color = Color(0xFF161719)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
        Text(text="Python code", fontSize = 19.sp, color = Color(0xFFf5f5f5), modifier = Modifier.align(Alignment.CenterVertically).padding(start =8.dp))
        UseButton(onSendClick = {})
    }
    Spacer(modifier = Modifier.height(6.dp))
}


@Composable
fun UseButton(onSendClick: () -> Unit) {
    IconButton(
        onClick = onSendClick,
        modifier = Modifier.size(20.dp)
            .background(color = Color(0xFFFFFFFF), shape = CircleShape)

    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_24), // Replace with your icon resource
            contentDescription = "Use",
            tint = Color(0xFF000000),
            modifier = Modifier.size(20.dp)
        )
    }
}
