package ai.soundcast.offlinegpt.View.Notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.soundcast.offlinegpt.Database.Note
import ai.soundcast.offlinegpt.Database.NoteDao
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(noteDao: NoteDao, onBack: () -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    fun saveNote() {
        coroutineScope.launch {
            if (title.text.isNotEmpty() || content.text.isNotEmpty()) {
                val note = Note(
                    title = title.text.ifBlank { "Untitled" },
                    content = content.text,
                    timestamp = System.currentTimeMillis()
                )
                noteDao.insert(note)
            }
        }
    }

    Surface(modifier = Modifier.background(color = Color(0xFF161719))) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("New Note", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            saveNote()
                            onBack()
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, tint = Color.White, contentDescription = "Back")
                        }
                    },
                    colors = topAppBarColors(containerColor = Color(0xFF040605))
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color(0xFF161719)),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BasicTextField(
                        value = title,

                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(end = 12.dp, start = 12.dp)
                            .background(Color(0xFF2A2B2E), shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize =22.sp),
                        decorationBox = {
                            innerTextField ->
                            if (title.text.isEmpty()) {

                                Text("Title", color = Color.Gray, fontSize = 22.sp)
                            }
                            innerTextField()
                        }
                    )

                    BasicTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(end = 12.dp, start = 12.dp)
                            .background(Color(0xFF2A2B2E), shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        decorationBox = { innerTextField ->
                            if (content.text.isEmpty()) {
                                Text("Note", color = Color.Gray, fontSize = 18.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }
        )
    }
}