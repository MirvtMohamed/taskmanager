package com.example.taskmanager.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithProjects(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val project: List<Project>
)