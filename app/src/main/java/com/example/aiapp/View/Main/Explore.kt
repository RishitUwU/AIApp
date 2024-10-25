package com.example.aiapp.View.Main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiapp.R
import com.example.aiapp.View.Notes.AllNotes
import com.example.aiapp.ui.theme.universoFontFamily


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExploreScreen() {


    val context = LocalContext.current


    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color(0xFF040605),
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Text("Explore", fontFamily = universoFontFamily, fontSize = 32.sp, fontWeight = FontWeight.Normal )
                    }
                )
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF040605))
                    .padding(paddingValues )
                    .padding(top = 8.dp, start = 8.dp, end =8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                            .clickable {
                                val intent  = Intent(context, AllNotes::class.java )
                                context.startActivity(intent) },

                        ){
                        BoxContent("Write and Summarize your notes.", iconResId = R.drawable.outline_writing)

                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    ){
                        BoxContent("Schedule your day", iconResId = R.drawable.outline_calendar)

                    }

                }
                Spacer(modifier = Modifier.size(8.dp))
                Box(modifier = Modifier.fillMaxWidth()
                    .size(180.dp)
                    .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                ){
                    BoxContent("Search anything offline", iconResId = R.drawable.outline_search)

                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    ){                        BoxContent("Write social media content", iconResId = R.drawable.outline_writing)
                    }

                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    ){
                        BoxContent("Chat with psychologist AI", iconResId = R.drawable.outline_brain)

                    }

                }

            }
        }
    }

}


@Composable
fun BoxContent(boxTextContent1: String, @DrawableRes iconResId: Int ){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = iconResId), // Replace with your icon
            contentDescription = "Icon",
            modifier = Modifier
                .size(74.dp)
                .padding(bottom = 8.dp, top = 28.dp, start = 16.dp),
            tint = Color(0xFF5abebc)
        )

        Text(
            text = boxTextContent1,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 4.dp, start = 20.dp)

        )
    }
}



