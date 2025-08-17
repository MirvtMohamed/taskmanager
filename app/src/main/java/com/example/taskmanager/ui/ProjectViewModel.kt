package com.example.taskmanager.ui

import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.taskmanager.data.entities.Project
import com.example.taskmanager.data.daos.ProjectDao
import com.example.taskmanager.data.entities.Task
import com.example.taskmanager.data.daos.TaskDao
import kotlinx.coroutines.flow.Flow

class ProjectViewModel(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao
) : ViewModel() {

    // Expose a query as LiveData (2.5)
    val allProjectsLiveData: LiveData<List<Project>> =
        projectDao.getAllProjectsFlow().asLiveData()

    // Expose a different query as Flow (2.5)
    fun tasksInProjectFlow(projectId: Int): Flow<List<Task>> =
        taskDao.getTasksForProjectFlow(projectId)

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val projectDao: ProjectDao,
        private val taskDao: TaskDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProjectViewModel(projectDao, taskDao) as T
        }
    }
}