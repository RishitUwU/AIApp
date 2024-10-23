package com.example.aiapp.Notes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiapp.R
import com.example.aiapp.ui.theme.universoFontFamily

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen(){
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF040605)) {
        Scaffold (topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF040605),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Notes", fontFamily = universoFontFamily, fontSize = 26.sp, fontWeight = FontWeight.Normal )
                },
                actions = {
                    IconButton(onClick = { /* Handle search icon click */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_search),
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }}
            )
        }, floatingActionButton = { CustomFloatingActionButton() },
            floatingActionButtonPosition = FabPosition.Center
        ){



            paddingValues ->


            LazyVerticalGrid  (  columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues).background(Color(0xFF040605)),
                contentPadding = PaddingValues(8.dp)
            ){

                items(count = 20) {
                    NoteItem(onClick = {})

                }

            }

        }

    }
}


@Composable
fun NoteItem(onClick: () -> Unit){

   // val randomHeight = Random.nextInt(110, 201).dp
    val randomHeight = 150.dp
    val lineHeight = 24.dp
    val maxLines = (randomHeight.value / lineHeight.value).toInt() - 2


    Box(modifier = Modifier.height(randomHeight).clickable { onClick }.padding(end = 8.dp, start = 8.dp, bottom = 12.dp).fillMaxWidth().background(color = Color(0xFF161719), shape = RoundedCornerShape(18.dp))){

        Column (modifier = Modifier.fillMaxHeight().padding(start = 14.dp, top = 12.dp)){
            Text(text = "27 Mar", color = Color(0xFF6f6f6f), fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = "Grocery List", color = Color(0xFFf5f5f5), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "* Milk\n* Egg\n* Bread\n* Butter", color =Color(0xFF8f8f8f), fontSize = 14.sp, maxLines = maxLines.coerceAtLeast(1),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))

        }
    }


}


@Composable
fun CustomFloatingActionButton(){
    FloatingActionButton(
        backgroundColor = Color(0xFFdcdcdc),
        onClick = {},

        modifier = Modifier
            .padding(bottom = 24.dp).height(40.dp),

        shape = RoundedCornerShape(8.dp), ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "Add Note",
                tint = Color(0xFF010101),
                modifier = Modifier.size(24.dp).padding(start = 8.dp)
            )
            Text(
                text = "Add new note",
                color = Color(0xFF010101),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}



