package ai.soundcast.offlinegpt.View.Chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun SocialMediaWriterScreen(){

    val message = remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                "Hello! How can I assist you?"
            )
        )
    }



}



