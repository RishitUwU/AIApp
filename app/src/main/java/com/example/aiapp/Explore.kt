package com.example.aiapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExploreScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color(0xFF040605),
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Text("Explore")
                    }
                )
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 70.dp,
                        start = 8.dp,
                        end = 8.dp )
                    .background(Color.White)
            ) {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    ){
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    )

                }
                Box(modifier = Modifier.fillMaxWidth()
                    .size(180.dp)
                    .padding(top = 8.dp)
                    .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                )
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(180.dp)
                            .background(Color(0xFF161719), shape = RoundedCornerShape(14.dp))
                    )

                }

            }
        }
    }

}



