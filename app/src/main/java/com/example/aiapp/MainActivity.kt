package com.example.aiapp

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.aiapp.Database.ModelDao
import com.example.aiapp.Database.SettingsDatabase
import com.example.aiapp.Model.InferenceModel
import com.example.aiapp.View.Chat.ChatScreen
import com.example.aiapp.View.Chat.PromptLibraryScreen
import com.example.aiapp.View.Main.ExploreScreen
import com.example.aiapp.View.Main.ProfileScreen
import com.example.aiapp.View.Main.SettingsScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {




    private var modelPath: String? = null
    private var downloadId: Long = 0L

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showPermissionToast()
        } else {
            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionToast()

            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        } else {
            showPermissionToast()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextCompat.registerReceiver(
            this,
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        val db = Room.databaseBuilder(
            applicationContext,
            SettingsDatabase::class.java,
            "settings_database"
        ).build()

        val modelDao = db.modelDao()

        enableEdgeToEdge()
        setContent {
            MyApp(modelDao)
        }


        requestStoragePermission()


    }



    private fun showPermissionToast() {
        Toast.makeText(this, "Do you want to allow access to your files?", Toast.LENGTH_LONG).show()
        AlertDialog.Builder(this)
            .setTitle("Access Files")
            .setMessage("Do you want to download the model file (falcon_cpu.bin)?")
            .setPositiveButton("Yes") { _, _ ->
                checkAndDownloadModel()

            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            .show()
    }


    private fun checkAndDownloadModel() {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "falcon_cpu.bin")
        if (file.exists()) {
            modelPath = file.path
            initializeModel()
        } else {
            downloadFile()
//            downloadAndSaveFile(
//                "https://s3.ap-south-1.amazonaws.com/www.soundcast.ai/falcon_cpu.bin",
//                "falcon_cpu.bin"
//            )
        }
    }



    private fun downloadFile() {
        val url = "https://s3.ap-south-1.amazonaws.com/www.soundcast.ai/falcon_cpu.bin"
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Downloading falcon_cpu.bin")
            setDescription("The LLM file is being downloaded.")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "falcon_cpu.bin")
        }

        val downloadManager = ContextCompat.getSystemService(this, DownloadManager::class.java)
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request)
        }
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "falcon_cpu.bin")
                if (file.exists()) {
                    modelPath = file.path
                    initializeModel()
                } else {
                    Toast.makeText(context, "Download failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun initializeModel() {
        modelPath?.let { path ->
            lifecycleScope.launch {
                // Load the model in the IO context to avoid blocking the main thread
                val modelInitialized = withContext(Dispatchers.IO) {
                    try {
                        InferenceModel.getInstance(this@MainActivity, path) // Load model
                        true // Return true if successful
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false // Return false if there was an error
                    }
                }

                if (modelInitialized) {
                    // Navigate to the chat screen after the model is successfully initialized
                    runOnUiThread {
//                        navController.navigate(CHAT_SCREEN) {
//                            popUpTo(START_SCREEN) { inclusive = true }
//                            launchSingleTop = true
//                        }
                    }
                } else {
                    // Handle model initialization failure (e.g., show an error message)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to initialize the model", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}
@Composable
fun ExploreNav(modelPath: String? = null) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "explore") {
        composable("explore") { ExploreScreen(navController) }
        composable("chat") { ChatScreen(navController, "Chat", modelPath = modelPath ?: "") }
        composable("socialMediaWriter") { ChatScreen(navController, "Social media writer", modelPath = modelPath ?: "") }
        composable("chatWithYourself") { ChatScreen(navController, "Chat with yourself", modelPath = modelPath ?: "") }
        composable("searchOnline") { ChatScreen(navController, "Search online", modelPath = modelPath ?: "") }
    }
}


@Composable
fun ChatNav(modelPath: String? = null) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "chat") {
        composable("chat") { ChatScreen(navController, "Chat", modelPath = modelPath ?: "") }
        composable("promptLibrary") { PromptLibraryScreen() }

    }
}

@Composable
fun MyApp(modelDao: ModelDao, modelPath: String? = null) {


    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Home", "Search","Settings", "Profile")
    val systemUiController = rememberSystemUiController()

    // Set the status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFF5abebc) // Light blue color
        )
    }


    Scaffold(
        bottomBar = {
            Column (modifier = Modifier){
                BottomNavigationBar(selectedItem = selectedItem, onItemSelected = { selectedItem = it })

            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                0 -> ExploreNav(modelPath = modelPath)
                1 -> ChatNav(modelPath = modelPath)
                2 -> SettingsScreen (modelDao = modelDao, onDeleteHistory = {})
                3 -> ProfileScreen()

            }
        }
    }
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
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_setting), contentDescription = "Settings", tint = if (selectedItem == 2) Color(0xFF5abebc) else Color.White, modifier = Modifier.size(24.dp)) },
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


