package ai.soundcast.offlinegpt.View.Main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(){
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        androidx.compose.material3.Scaffold(topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF040605),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Normal)
                }
            )
        }, containerColor = Color(0xFF040605)
            ) { paddingValues ->

            Column (modifier = Modifier.fillMaxHeight().padding(paddingValues).background(color = Color(0xFF040605))){
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Upgrade your account", color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "upgrade account", tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                }
            }
    } }
}