package com.example.aiapp.View.Chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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



    Surface(modifier = Modifier.fillMaxSize(),color = Color(0xFF040605)) {
        Column (modifier = Modifier.fillMaxSize()){

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    ChatBubble(message = msg)
                }
            }

            LazyRow (modifier = Modifier.fillMaxWidth()){
                item {
                    SocialMediaNameBox("Instagram")
                    SocialMediaNameBox()
                    SocialMediaNameBox("LinkedIn")
                    SocialMediaNameBox("Twitter")
                    SocialMediaNameBox("Youtube")
                }
            }


            ChatBox(messageState = message, onSendClick = {
                if (message.value.isNotEmpty()) {
                    messages = messages.toMutableList().apply { add(message.value) }
                    message.value = ""
                }
            }, onSpeechToTextClick={} )
        }
    }
}

@Composable
fun SocialMediaNameBox(socialMediaName : String = "Facebook"){
    Spacer(Modifier.width(8.dp))
    Box(modifier = Modifier.background(shape = RoundedCornerShape(28.dp), color = Color(0xFF161719))){
        Text(text = socialMediaName, color = Color(0xFFf5f5f5), fontSize = 18.sp, modifier = Modifier.padding(horizontal =10.dp).padding(8.dp))
    }

}


