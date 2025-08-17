package com.example.taskmanager.ui



import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.data.entities.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp(
    viewModel: ProjectViewModel,
    onProjectClick: (Project) -> Unit
) {

    val projects by viewModel.allProjectsLiveData.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager Projects") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->


        LaunchedEffect(projects) {
            Log.d("Compose_TEST", "Compose recomposed with ${projects.size} projects")
            projects.forEach { project ->
                Log.d("Compose_TEST", "Compose rendering project: ${project.title}")
            }
        }

        if (projects.isEmpty()) {
            // Show loading or empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading projects...")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(projects) { project ->
                    ProjectCard(
                        project = project,
                        viewModel = viewModel,
                        onProjectClick = onProjectClick
                    )
                }
            }
        }
    }
}

