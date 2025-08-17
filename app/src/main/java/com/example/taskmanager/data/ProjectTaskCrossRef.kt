package com.example.taskmanager.data

import android.system.Int64Ref
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
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("taskId")]
)
data class ProjectTaskCrossRef(
    val projectId: Int,
    val taskId: Int
)
