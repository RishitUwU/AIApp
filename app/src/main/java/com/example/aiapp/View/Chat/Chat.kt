package com.example.aiapp.View.Chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aiapp.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController) {
    var isPromptLibraryVisible by remember { mutableStateOf(true) }
    val message = remember { mutableStateOf("") }
    var isTTS by remember { mutableStateOf(false) }

    var messages by remember {
        mutableStateOf(
            listOf(
                "Hello! How can I assist you?"
            )
        )
    }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current // Get the context here

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Scaffold (topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF040605),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Chat", fontSize = 24.sp, fontWeight = FontWeight.Normal )
                },
                actions = {
                    IconButton(
                        onClick = { isTTS=!isTTS },
                        modifier = Modifier
                            .size(54.dp)
                            .padding(end = 16.dp)
                            .background(
                                color = if (isTTS) Color.White else Color(0xFF161719),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_volume_up_24),
                            contentDescription = "Speech to text",
                            tint = if (isTTS) Color.Black else Color(0xFF5abebc),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }


            )
        }){ paddingValues ->

            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF040605))) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(paddingValues)
                        .padding(8.dp),
                    reverseLayout = true
                ) {
                    items(messages.reversed()) { msg ->
                        ChatBubble(message = msg)
                    }
                }


                if (isPromptLibraryVisible){
                    PromptLibrary(navController)

                }

                ChatBox(messageState = message, onSendClick = {
                    if (message.value.isNotEmpty()) {
                        isPromptLibraryVisible = !isPromptLibraryVisible
                        messages = messages.toMutableList().apply { add(message.value) }
                        message.value = ""

                    }
                }, onSpeechToTextClick = {
                    showDialog = true
                })


                if (isTTS) {
                    rememberTTS(context, isTTS)?.speak(message.value, TextToSpeech.QUEUE_FLUSH, null, "TTS_MESSAGE_ID")
                }

                // Show Speech to Text dialog
                if (showDialog) {
                    SpeechToTextDialog(onDismiss = { showDialog = false }, context = context) { recognizedText ->
                        message.value = recognizedText
                        showDialog = false
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(messageState: MutableState<String>, onSendClick: () -> Unit, onSpeechToTextClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageState.value,
            onValueChange = { messageState.value = it },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(28.dp),
            placeholder = { Text("Type your message...", modifier = Modifier.padding(start = 10.dp), color = Color.White) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF787b7c),
                focusedTextColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = false,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start // Ensures text is left-aligned
            )
        )

        Spacer(modifier = Modifier.width(8.dp))
        SpeechToTextButton(onClick = onSpeechToTextClick)
        Spacer(modifier = Modifier.width(8.dp))
        SendButton { onSendClick() }
    }
}

@Composable
fun SpeechToTextButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(54.dp)
            .background(color = Color(0xFF161719), shape = RoundedCornerShape(100.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_mic_none_24), // Replace with your icon resource
            contentDescription = "Speech to text",
            tint = Color(0xFF5abebc),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun SpeechToTextDialog(onDismiss: () -> Unit, context: Context, onResult: (String) -> Unit) {
    var isListening by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Speech to Text") },
        text = {
            Column {
                Text("Tap the button and start speaking...")
                Spacer(modifier = Modifier.height(8.dp))
                if (isListening) {
                    Text("Listening...", color = Color.Red)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                isListening = true
                startSpeechToText(context) { recognizedText ->
                    onResult(recognizedText)
                    isListening = false // Close dialog after getting text
                }
            }) {
                Text("Start Listening")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun startSpeechToText(context: Context, onResult: (String) -> Unit) {
    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
    }

    recognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            Toast.makeText(context, "Error occurred: $error", Toast.LENGTH_SHORT).show()
            recognizer.destroy()
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null && matches.isNotEmpty()) {
                onResult(matches[0]) // Return the first result
            }
            recognizer.destroy()
        }

        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    recognizer.startListening(intent)
}

@Composable
fun ChatBubble(message: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(Color(0xFF161719), shape = RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.bot),
                contentDescription = "Bot Icon",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(width = 40.dp, height = 40.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(100.dp))
            )
            Text(text = message, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))
        }

        Icon(
            painter = painterResource(id = R.drawable.outline_copy),
            contentDescription = "Copy",
            tint = Color(0xFF5abebc),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(14.dp)
        )
    }
}

@Composable
fun SendButton(onSendClick: () -> Unit) {
    IconButton(
        onClick = onSendClick,
        modifier = Modifier
            .size(54.dp)
            .background(color = Color(0xFF161719), shape = RoundedCornerShape(100.dp)) // Adjust the size of the button
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_send), // Replace with your icon resource
            contentDescription = "Send",
            tint = Color(0xFF5abebc), // Color of the icon
            modifier = Modifier.size(20.dp) // Size of the icon
        )
    }
}

@Composable
fun PromptLibrary(navController: NavHostController) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Prompt library",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // Adjust the color according to your theme
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_24), // Replace with your icon resource
                contentDescription = "Go",
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .padding(8.dp) // Add background to the icon
                    .clickable {
                        navController.navigate("promptLibrary")
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun TagButton(text: String) {
    Button(
        onClick = { /* Handle click */ },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF161719),
            contentColor = Color.White
        ),
        modifier = Modifier
            .height(40.dp)
    ) {
        Text(text = text, fontSize = 14.sp)
    }
}



@Composable
fun rememberTTS(context: Context, isTTS: Boolean): TextToSpeech? {
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(isTTS) {
        if (isTTS) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.getDefault()
                }
            }
        } else {
            tts?.shutdown()
            tts = null
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.shutdown()
        }
    }

    return tts
}