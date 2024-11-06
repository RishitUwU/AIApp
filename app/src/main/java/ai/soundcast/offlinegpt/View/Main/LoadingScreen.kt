package ai.soundcast.offlinegpt.View.Main

import ai.soundcast.offlinegpt.DownloadFileDialog
import ai.soundcast.offlinegpt.downloadFileIfNotExists
import ai.soundcast.offlinegpt.others.InferenceModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
internal fun LoadingRoute(
    onModelLoaded: () -> Unit = { }
) {
    val context = LocalContext.current.applicationContext
    var errorMessage by remember { mutableStateOf("") }
    var isDownloading by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (errorMessage != "") {
        ErrorMessage("Model not found...")

        LaunchedEffect(Unit) {
            val file = File(context.filesDir, "model.bin")

            if (!file.exists()) {
                showConfirmationDialog = true

            } else if (file.exists() && file.length()!=1346559040L){
                file.delete()
                showConfirmationDialog = true
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
        isDownloaded=true
        if (isDownloaded){

            LaunchedEffect(Unit, block = {
                withContext(Dispatchers.IO) {
                    try {
                        InferenceModel.getInstance(context)
                        // Notify the UI that the model has finished loading
                        withContext(Dispatchers.Main) {
                            onModelLoaded()
                        }
                    } catch (e: Exception) {
                        errorMessage = e.localizedMessage ?: "UnknownError"

                    }
                }
            })

        }


    } else {
        LoadingIndicator()
    }

    LaunchedEffect(Unit, block = {
        withContext(Dispatchers.IO) {
            try {
                InferenceModel.getInstance(context)
                // Notify the UI that the model has finished loading
                withContext(Dispatchers.Main) {
                    onModelLoaded()
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "UnknownError"

            }
        }
    })
}

@Composable
fun LoadingIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.background(color = Color(0xFF161719)).fillMaxSize()
    ) {
        Text(
            text = "loading model...",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 8.dp)

        )
        CircularProgressIndicator(color = Color(0xFF5abebc))
    }
}

@Composable
fun ErrorMessage(
    errorMessage: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.background(color = Color(0xFF161719))
            .fillMaxSize()
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center

        )
    }
}
