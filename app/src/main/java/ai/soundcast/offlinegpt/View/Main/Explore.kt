package ai.soundcast.offlinegpt.View.Main

import ai.soundcast.offlinegpt.R
import ai.soundcast.offlinegpt.View.Notes.AllNotes
import ai.soundcast.offlinegpt.ui.theme.universoFontFamily
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import java.io.RandomAccessFile


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavHostController) {


    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val accentColor = Color(0xFF5abebc)
    val disabledColor = Color.Gray.copy(alpha = 0.5f)

    val totalRamInBytes = getTotalRAM(context)
    val totalRamInMb = totalRamInBytes / (1024 * 1024)
    val totalRamInGb:Double = totalRamInBytes.toDouble() / (1024 * 1024 * 1024)

    Log.d("TAG", totalRamInGb.toString())
    if (totalRamInGb<5.0){
        Toast.makeText(context, "You need minimum 6GB of ram to run this application", Toast.LENGTH_LONG).show()
    }


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
                    .padding(paddingValues)
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            ) {

                SearchBar(navController)
                Spacer(modifier = Modifier.height(16.dp))
                FeatureCards(accentColor, disabledColor, navController, context)


            }
        }
    }

}

@Composable
fun BoxContent(boxTextContent1: String, @DrawableRes iconResId: Int, color: Color=Color.White){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = iconResId), // Replace with your icon
            contentDescription = "Icon",
            modifier = Modifier
                .size(74.dp)
                .padding(bottom = 8.dp, top = 28.dp, start = 16.dp),
            tint = if(color==Color.White) Color(0xFF5abebc) else Color(0xFF9b9b9b)
        )

        Text(
            text = boxTextContent1,
            fontSize = 14.sp,
            color =color,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 4.dp, start = 20.dp)

        )
    }
}

fun getTotalRAM(context: Context): Long {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        memoryInfo.totalMem // returns total RAM in bytes
    } else {
        // For older devices, you may need to parse from /proc/meminfo
        getTotalRAMFromProcMemInfo()
    }
}

fun getTotalRAMFromProcMemInfo(): Long {
    val reader = RandomAccessFile("/proc/meminfo", "r")
    val line = reader.readLine()
    reader.close()
    val totalMemoryKb = line.replace(Regex("[^0-9]"), "").toLong()
    return totalMemoryKb * 1024
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var isOffline by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFF1C1C1C), shape = RoundedCornerShape(28.dp)),
            placeholder = { Text("Search online and offline", color = Color.Gray) },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(24.dp),
            trailingIcon = {
                IconButton(onClick = {
                    Log.d("TAG", "SearchBar: $searchText")
                    navController.navigate("start_screen2?searchText=${Uri.encode(searchText)}")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_send),
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF5abebc)
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                focusedPlaceholderColor = Color.Gray,
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Offline toggle switch
        Switch(
            checked = isOffline,
            onCheckedChange = {
                isOffline = false
                Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show()
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF5abebc),
                checkedTrackColor = Color(0xFF5abebc).copy(alpha = 0.5f)
            )
        )

        Text(
            text = "Online",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
fun FeatureCards(accentColor: Color, disabledColor: Color, navController: NavHostController, context: Context) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FeatureCard("Write & Summarize", "Create and condense your notes efficiently", accentColor, enabled = true, iconResId = R.drawable.outline_writing, onClick = {
            val intent = Intent(context, AllNotes::class.java)
            context.startActivity(intent)
        })
        FeatureCard("Schedule Your Day", "Plan and organize your daily activities", disabledColor, enabled = false, iconResId = R.drawable.outline_calendar, onClick = {
            Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show()
        })
        FeatureCard("Social Content", "Create engaging social media posts", accentColor, enabled = true, iconResId = R.drawable.outline_chat, onClick = {
            navController.navigate("socialMediaWriter")
        })
        FeatureCard("Self Chat", "Reflect and brainstorm with yourself", disabledColor, enabled = false, iconResId = R.drawable.outline_brain, onClick = {
            Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show()
        })
    }
}

@Composable
fun FeatureCard(title: String, description: String, iconColor: Color, enabled: Boolean, @DrawableRes iconResId: Int,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = Color(0xFF1f2937)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF040605))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .alpha(if (enabled) 1f else 0.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Replace with actual icon
                Icon(
                    painter = painterResource(id = iconResId), // Replace with your icon
                    contentDescription = "Icon",
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )

            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 19.sp)
                Text(text = description, color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

