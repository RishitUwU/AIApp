package com.example.aiapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ChatScreen() {
    val message = remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                "Hello! How can I assist you?"
            )
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Messages list
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

            ChatBox(messageState = message, onSendClick = {
                if (message.value.isNotEmpty()) {
                    messages = messages.toMutableList().apply { add(message.value) }
                    message.value = ""
                }
            })

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(messageState: MutableState<String>, onSendClick: () -> Unit) {
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
        SendButton { onSendClick() }

    }
}
@Composable
fun ChatBubble(message: String) {
    Box(
        modifier = Modifier

            .padding(4.dp)
            .background(Color(0xFF161719), shape = RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {

        Row ( verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {


            Image(
                painter = painterResource(R.drawable.bot),
                contentDescription = "Bot Icon",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(width = 40.dp, height = 40.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(100.dp))
            )
            Text(text = message, fontSize = 16.sp, color = Color.White,modifier = Modifier.weight(1f))
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
fun SendButton(onSendClick: () -> Unit){
    IconButton(
        onClick = onSendClick,
        modifier = Modifier.size(54.dp).background(color = Color(0xFF161719), shape = RoundedCornerShape(100.dp)), // Adjust the size of the button
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_send), // Replace with your icon resource
            contentDescription = "Send",
            tint = Color(0xFF5abebc), // Color of the icon
            modifier = Modifier.size(20.dp) // Size of the icon
        )
    }
}