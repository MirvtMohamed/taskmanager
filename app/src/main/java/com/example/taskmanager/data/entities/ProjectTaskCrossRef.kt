package com.example.taskmanager.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "project_task_cross_ref",
    primaryKeys = ["projectId","taskId"],
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
    ],
    indices = [Index("taskId")]
)
data class ProjectTaskCrossRef(
    val projectId: Int,
    val taskId: Int
)