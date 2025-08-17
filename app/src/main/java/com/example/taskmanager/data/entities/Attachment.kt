package com.example.taskmanager.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("taskId")]
)

data class Attachment(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val filePath: String,
    val taskId: Int
)