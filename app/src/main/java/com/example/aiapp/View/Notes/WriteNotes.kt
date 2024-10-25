package com.example.aiapp.View.Notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.aiapp.Database.Note
import com.example.aiapp.Database.NoteDao
import kotlinx.coroutines.launch

@Composable
fun WriteNotes (){
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(noteDao: NoteDao, onSave: () -> Unit) {
    var noteContent by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Note") },
                colors = topAppBarColors(containerColor = Color(0xFF040605))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BasicTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161719), shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                Button(
                    onClick = {
                        val title = noteContent.text.split(" ").firstOrNull() ?: "Untitled"
                        val note = Note(
                            title = title,
                            content = noteContent.text,
                            timestamp = System.currentTimeMillis())

                        coroutineScope.launch {
                            noteDao.insert(note)
                            onSave()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save Note")
                }
            }
        }
    )
}