package com.example.taskmanager.ui



import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.data.AppDatabase
import com.example.taskmanager.data.entities.Project
import com.example.taskmanager.data.entities.Task
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen() {
    // Provide ViewModel
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModel.Factory(db.projectDao(), db.taskDao())
    )

    // Observe LiveData as state
    val projects by viewModel.allProjectsLiveData.observeAsState(emptyList())

    // Collect Flow as state
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    val projectId = 1 // demo projectId
    LaunchedEffect(projectId) {
        viewModel.tasksInProjectFlow(projectId).collectLatest { taskList ->
            Log.d("COMPOSE", "Collected Flow tasks: $taskList")
            tasks = taskList
        }
    }

    // UI Layout
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Projects & Tasks") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text("Observed LiveData Projects:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(projects) { project ->
                    ProjectItem(project)
                }
            }

            Divider()

            Text("Collected Flow Tasks:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(tasks) { task ->
                    TaskItem(task)
                }
            }
        }
    }
}

@Composable
fun ProjectItem(project: Project) {
    Text(
        text = project.title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun TaskItem(task: Task) {
    Text(
        text = task.description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(8.dp)
    )
}
