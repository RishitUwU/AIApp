package com.example.aiapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.aiapp.Database.ModelDao
import com.example.aiapp.Database.SettingsDatabase
import com.example.aiapp.View.Chat.ChatRoute
import com.example.aiapp.View.Chat.PromptLibraryScreen
import com.example.aiapp.View.Main.ExploreScreen
import com.example.aiapp.View.Main.LoadingRoute
import com.example.aiapp.View.Main.ProfileScreen
import com.example.aiapp.View.Main.SettingsScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val db = Room.databaseBuilder(
            applicationContext,
            SettingsDatabase::class.java,
            "settings_database"
        ).build()

        val modelDao = db.modelDao()

        enableEdgeToEdge()
        setContent {
            MyApp(modelDao, context = applicationContext)
        }




    }




}
@Composable
fun ExploreNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "explore") {
        composable("explore") { ExploreScreen(navController) }
        composable(
            route = "chat?promptText={promptText}",
            arguments = listOf(navArgument("promptText") { defaultValue = "" })
        ) { backStackEntry ->
            val promptText = backStackEntry.arguments?.getString("promptText") ?: ""
            ChatRoute(navController = navController, chatScreenTitle = "Chat", promptText = promptText)
        }
        composable("start_screen") { LoadingRoute(onModelLoaded = {navController.navigate("chat")}) }
        composable("promptLibrary") { PromptLibraryScreen(navController=navController) }
        composable("socialMediaWriter") { ChatRoute(navController =navController, chatScreenTitle ="Social media writer") }
        composable("chatWithYourself") { ChatRoute(navController =navController, chatScreenTitle ="Chat with yourself") }
        composable("searchOnline") { ChatRoute(navController =navController, chatScreenTitle ="Search online") }
    }
}


@Composable
fun ChatNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start_screen") {
        composable("start_screen") { LoadingRoute(onModelLoaded = {navController.navigate("chat")}) }
        composable(
            route = "chat?promptText={promptText}",
            arguments = listOf(navArgument("promptText") { defaultValue = "" })
        ) { backStackEntry ->
            val promptText = backStackEntry.arguments?.getString("promptText") ?: ""
            ChatRoute(navController = navController, chatScreenTitle = "Chat", promptText = promptText)
        }
        composable("promptLibrary") { PromptLibraryScreen(navController=navController) }

    }
}


@Composable
fun DownloadFileDialog(isDownloading: Boolean, progress: Int) {
    if (isDownloading) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Downloading File...", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Progress: $progress%")
            }
        }
    }
}



fun downloadFileIfNotExists(
    context: Context,
    fileName: String,
    fileUrl: String,
    onDownloadComplete: (Boolean) -> Unit,
    onProgressUpdate: (Int) -> Unit
) {
    val internalFile = File(context.filesDir, fileName)
    if (internalFile.exists()) {
        onDownloadComplete(true)
        return
    }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val totalLength = connection.contentLength
                Log.d("TAG", "total length: $totalLength")
                var totalBytesRead = 0
                val inputStream: InputStream = connection.inputStream
                FileOutputStream(internalFile).use { output ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var progress = 0

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead

                        val newProgress = (totalBytesRead.toDouble() / totalLength.toDouble()) * 100

                        progress=newProgress.toInt()
                        withContext(Dispatchers.Main) {
                            onProgressUpdate(progress)
                        }


                    }


                }
                inputStream.close()
                onDownloadComplete(true)
            } else {
                onDownloadComplete(false)
            }

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            onDownloadComplete(false)
        }
    }
}




@Composable
fun MyApp(modelDao: ModelDao, context: Context) {
    val activity = LocalContext.current as? Activity
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Search", "Settings", "Profile")
    val systemUiController = rememberSystemUiController()


    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "model.bin")
        if (!file.exists()) {
            showConfirmationDialog = true
        }
    }

    // Set the status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFF5abebc) // Light blue color
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedItem = selectedItem, onItemSelected = { selectedItem = it })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                0 -> ExploreNav()
                1 -> ChatNav()
                2 -> SettingsScreen(modelDao = modelDao, onDeleteHistory = {})
                3 -> ProfileScreen()
            }
        }
    }

    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "model.bin")
        if (!file.exists()) showConfirmationDialog = true
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Model Not Found") },
            text = { Text("The model file is not downloaded. Would you like to download it?") },
            confirmButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                    isDownloading = true
                    downloadFileIfNotExists(
                        context,
                        "model.bin",
                        "https://offlineai.s3.ap-south-1.amazonaws.com/gemma-2b-it-cpu-int4.bin",
                        onDownloadComplete = { success ->
                            isDownloading = false
                            if (!success) Log.d("Download progress", "Download failed.")
                        },
                        onProgressUpdate = { progress ->
                            downloadProgress = progress
                        }
                    )
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmationDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    DownloadFileDialog(
        isDownloading = isDownloading,
        progress = downloadProgress,
    )
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    BottomNavigation(
        backgroundColor = Color(0xFF040605),
        contentColor = Color.White

    ) {
        BottomNavigationItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_explore), contentDescription = "Home", tint = if (selectedItem == 0) Color(0xFF5abebc) else Color.White, modifier = Modifier.size(20.dp)) },
        )
        BottomNavigationItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_chat), contentDescription = "Search",tint = if (selectedItem == 1) Color(0xFF5abebc) else Color.White, modifier = Modifier.size(24.dp)) },
        )

        BottomNavigationItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_profile), contentDescription = "Profile", tint = if (selectedItem == 3) Color(0xFF5abebc) else Color.White, modifier = Modifier.size(24.dp)) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {

}


