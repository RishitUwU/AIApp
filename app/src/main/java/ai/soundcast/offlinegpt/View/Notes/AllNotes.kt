package ai.soundcast.offlinegpt.View.Notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import ai.soundcast.offlinegpt.Database.Note
import ai.soundcast.offlinegpt.Database.NoteDao
import ai.soundcast.offlinegpt.Database.NoteDatabase
import ai.soundcast.offlinegpt.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class AllNotes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val db = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "note_database"
        ).build()

        val noteDao = db.noteDao()

        setContent {
            MainNavigation(noteDao)

        }
    }
}




@Composable
fun MainNavigation(noteDao: NoteDao) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "notes") {
        composable("notes") {
            NotesScreen(noteDao, onNewNoteClick = {
                navController.navigate("newNote")
            })
        }
        composable("newNote") {
            NewNoteScreen(
                noteDao,
                onBack = {
                    navController.popBackStack()

                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(noteDao: NoteDao, onNewNoteClick: () -> Unit) {
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var filteredNotes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            notes = noteDao.getAllNotes()
            filteredNotes = notes // Initially, display all notes
        }
    }

    fun filterNotes(query: String) {
        searchQuery = query
        filteredNotes = if (query.isEmpty()) {
            notes // Show all notes if search query is empty
        } else {
            notes.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        androidx.compose.material.Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearching) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { filterNotes(it) },
                                placeholder = { Text("Search notes...", color = Color.Gray) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF161719), RoundedCornerShape(16.dp)),
                                textStyle = LocalTextStyle.current.copy(color = Color.White),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent, // Remove the underline
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        } else {
                            Text("Notes", color = Color.White)
                        }
                    },
                    colors = topAppBarColors(containerColor = Color(0xFF040605)),
                    actions = {
                        IconButton(onClick = {
                            isSearching = !isSearching
                            if (!isSearching) filterNotes("") // Reset search when closing
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_search),
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
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
                items(filteredNotes.size) { index ->
                    val note = filteredNotes[index]
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
            Spacer(modifier = Modifier.height(4.dp))
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
    val date = Date(this)
    val format = SimpleDateFormat("dd MMM, yyyy")
    return format.format(date)
}



