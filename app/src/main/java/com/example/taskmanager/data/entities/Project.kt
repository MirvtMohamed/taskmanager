package com.example.taskmanager.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("ownerId")]


)

data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val title: String,
    val ownerId:  Int,
    // Demonstrates List<String> converter
    val labels: List<String> = emptyList()
)