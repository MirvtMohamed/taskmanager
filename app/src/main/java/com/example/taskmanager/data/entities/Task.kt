package com.example.taskmanager.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("projectId")]
)

data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val projectId: Int,
    // canonical/primary project for the task
    // Demonstrates Date converter
    val dueDate: Date? = null
)