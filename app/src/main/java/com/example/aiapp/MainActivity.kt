package com.example.aiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}


@Composable
fun MyApp() {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Home", "Search","Settings", "Profile")

    Scaffold(
        bottomBar = {
            Column (modifier = Modifier.padding(bottom = 8.dp)){
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
            Text(text = "Selected: ${items[selectedItem]}", fontSize = 24.sp)
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
            icon = { Icon(painter = painterResource(id = R.drawable.outline_explore), contentDescription = "Home", tint = Color.White, modifier = Modifier.size(20.dp)) },
        )
        BottomNavigationItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_chat), contentDescription = "Search", tint = Color.White, modifier = Modifier.size(24.dp)) },
        )
        BottomNavigationItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(1) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_setting), contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(24.dp)) },
        )
        BottomNavigationItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(2) },
            icon = { Icon(painter = painterResource(id = R.drawable.outline_profile), contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(24.dp)) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}