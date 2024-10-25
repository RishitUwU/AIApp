package com.example.aiapp.View.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aiapp.Database.ModelDao
import com.example.aiapp.Database.SettingsDb

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
        TextButton(onClick = { expanded = true }) {
            Text(text = "Selected Model: $selectedModel", modifier = Modifier.weight(1f), color = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            models.forEach { model ->
                DropdownMenuItem(
                    text = { Text(model, color = Color.White) },
                    onClick = {
                        onModelChange(model)
                        expanded = false
                    }
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