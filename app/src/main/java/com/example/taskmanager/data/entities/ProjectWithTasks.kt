package com.example.taskmanager.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProjectWithTasks(
    @Embedded
    val project: Project,
    @Relation(
        entity = Task::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProjectTaskCrossRef::class,
            parentColumn = "projectId",
            entityColumn = "taskId"
        )
    )
    val task: List<Task>
)