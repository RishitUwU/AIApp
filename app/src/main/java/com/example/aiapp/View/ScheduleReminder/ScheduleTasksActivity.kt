package com.example.aiapp.View.ScheduleReminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiapp.Database.Task
import com.example.aiapp.Database.TaskDao
import com.example.aiapp.Database.TaskDatabase
import com.example.aiapp.Receiver.NotificationReceiver
import com.example.aiapp.View.Notes.toDateString
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleTasksActivity : ComponentActivity() {

    private lateinit var taskDao: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskDao = TaskDatabase.getDatabase(applicationContext).taskDao()

        enableEdgeToEdge()
        setContent {
            MainScreen(taskDao)

        }
    }
}

@Composable
fun MainScreen(taskDao: TaskDao) {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        tasks = taskDao.getAllTasks()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Show Dialog to Add Task */ }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(tasks) { task ->
                TaskItem(task)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = task.name.ifEmpty { "Untitled" }, fontSize = 20.sp)
        Text(text = "Date: ${task.date.toDateString()} Time: ${task.time.toTimeString()}", fontSize = 14.sp)
    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAddTask: (String, Long, Long) -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var time by remember { mutableStateOf(System.currentTimeMillis()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column {
                TextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") }
                )
                // Date and Time Picker UI components will go here
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (taskName.isEmpty()) taskName = "Untitled"
                onAddTask(taskName, date, time)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun scheduleNotification(context: Context, task: Task) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("task_name", task.name)
    }
    val pendingIntent = PendingIntent.getBroadcast(context, task.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Schedule the alarm
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        task.date + task.time,
        pendingIntent
    )
}
fun Long.toDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

fun Long.toTimeString(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(this)
}