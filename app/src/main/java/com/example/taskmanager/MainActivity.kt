package com.example.taskmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.data.AppDatabase
import com.example.taskmanager.data.entities.Project
import com.example.taskmanager.data.SampleDataRunner
import com.example.taskmanager.ui.ProjectCard
import com.example.taskmanager.ui.ProjectViewModel
import com.example.taskmanager.ui.TaskManagerApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ProjectViewModel

    companion object {
        private const val TAG = "MainActivity_TEST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModel with proper factory
        val db = AppDatabase.get(this)
        val factory = ProjectViewModel.Factory(db.projectDao(), db.taskDao())
        viewModel = ViewModelProvider(this, factory)[ProjectViewModel::class.java]


        SampleDataRunner.seedAndDemo(this)


        setContent {
            MaterialTheme {
                TaskManagerApp(
                    viewModel = viewModel,
                    onProjectClick = { project ->
                        //  Demonstrate lifecycle-aware Flow collection with logging
                        collectTasksForProject(project.id)
                    }
                )
            }
        }

        //  Traditional lifecycle observation with logging

        viewModel.allProjectsLiveData.observe(this) { projects ->
            Log.d(TAG, "Activity LiveData observed projects: $projects")
            Log.d(TAG, "Number of projects observed: ${projects.size}")
            projects.forEach { project ->
                Log.d(TAG, "Project observed: ${project.title} (ID: ${project.id})")
            }
        }
    }

    //  Demonstrate lifecycleScope.launch with collectLatest
    private fun collectTasksForProject(projectId: Int) {
        lifecycleScope.launch {
            viewModel.tasksInProjectFlow(projectId).collectLatest { tasks ->
                Log.d(TAG, "Activity Flow collected tasks for project $projectId: $tasks")
                Log.d(TAG, "Number of tasks collected: ${tasks.size}")
                tasks.forEach { task ->
                    Log.d(TAG, "Activity collected task: ${task.description} (Due: ${task.dueDate})")
                }
            }
        }
    }
}

