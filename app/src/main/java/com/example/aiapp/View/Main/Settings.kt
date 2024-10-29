package com.example.aiapp.View.Main

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aiapp.Database.ModelDao
import com.example.aiapp.Database.SettingsDb
import com.example.aiapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modelDao: ModelDao, onDeleteHistory: () -> Unit) {
    var selectedModel by remember { mutableStateOf<String>("") }
    val coroutineScope = rememberCoroutineScope()

    // Load current model when the screen is launched
    LaunchedEffect(Unit) {
        val currentModel = modelDao.getCurrentModel()
        selectedModel = currentModel?.name ?: "Default Model" // Default model if none set
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF040605))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        // AI Model Change Dropdown
        val models = listOf("Default Model", "Model A", "Model B", "Model C") // Example models
        DropdownMenu(
            modifier = Modifier.weight(1f),
            models,
            selectedModel,
            onModelChange = { newModel ->
                selectedModel = newModel
                coroutineScope.launch {
                    modelDao.insert(SettingsDb(name = newModel))
                }
            }
        )

        CustomButton (buttonText = "Delete History", onClick = { onDeleteHistory() })


        CustomButton (buttonText = "Request a feature", onClick = {  })


    }
}

@Composable
fun DropdownMenu(
    modifier: Modifier= Modifier,
    models: List<String>,
    selectedModel: String,
    onModelChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selected Model: ",
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24), // Use your dropdown icon here
                        contentDescription = "Dropdown",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp)) // Optional space between icon and text
                    Text(
                        text = selectedModel,
                        color = Color.White
                    )
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black, shape = RoundedCornerShape(8.dp))
        ) {
            models.forEach { model ->
                DropdownMenuItem(
                    text = { Text(model, color = Color.White) },
                    onClick = {
                        onModelChange(model)
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                )
            }
        }
    }
}


@Composable
fun CustomButton(buttonText : String, onClick: () -> Unit){

    Button(onClick = {  }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
        contentColor = Color.White,
        containerColor = Color(0xFF5abebc)

    )) {
        Text(buttonText)
    }
}