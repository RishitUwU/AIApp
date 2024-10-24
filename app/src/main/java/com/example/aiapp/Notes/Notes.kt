package com.example.aiapp.Notes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiapp.R
import com.example.aiapp.ui.theme.universoFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(noteDao: NoteDao, onNewNoteClick: () -> Unit) {
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            notes = noteDao.getAllNotes()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Notes", color = Color.White) },
                    colors = topAppBarColors(containerColor = Color(0xFF040605)),
                    actions = {
                        IconButton(onClick = { /* Handle search */ }) {
                            Icon(painter = painterResource(id = R.drawable.outline_search), contentDescription = "Search", tint = Color.White)
                        }
                    }
                )
            },
            floatingActionButton = { CustomFloatingActionButton(onClick = onNewNoteClick) },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFF040605)),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(notes.size) { index ->
                    val note = notes[index]
                    NoteItem(note, onClick = {})
                }
            }
        }
    }
}


@Composable
fun CustomFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        backgroundColor = Color(0xFFdcdcdc),
        onClick = onClick,  // Accept the onClick parameter and pass it to the FloatingActionButton

        modifier = Modifier
            .padding(bottom = 24.dp)
            .height(40.dp),

        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "Add Note",
                tint = Color(0xFF010101),
                modifier = Modifier.size(24.dp).padding(start = 8.dp)
            )
            Text(
                text = "Add new note",
                color = Color(0xFF010101),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}



@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .clickable { onClick() }
            .padding(end = 8.dp, start = 8.dp, bottom = 12.dp)
            .fillMaxWidth()
            .background(color = Color(0xFF161719), shape = RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.fillMaxHeight().padding(start = 14.dp, top = 12.dp)) {
            Text(text = note.timestamp.toDateString(), color = Color(0xFF6f6f6f), fontSize = 12.sp)
            Text(text = note.title, color = Color(0xFFf5f5f5), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                color = Color(0xFF8f8f8f),
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun Long.toDateString(): String {
    // Convert timestamp to readable date
    val date = java.util.Date(this)
    val format = java.text.SimpleDateFormat("dd MMM, yyyy")
    return format.format(date)
}

