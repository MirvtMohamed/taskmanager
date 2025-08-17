package com.example.taskmanager.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithAttachments(
    @Embedded
    val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val attachment: List<Attachment>
)