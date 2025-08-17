package com.example.taskmanager.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.taskmanager.data.entities.Task
import com.example.taskmanager.data.entities.TaskWithAttachments
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(tasks: List<Task>): List<Long>

    // Flow of tasks for a given project using the canonical projectId
    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY id ASC")
    fun getTasksForProjectFlow(projectId: Int): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskWithAttachments(taskId: Int): List<TaskWithAttachments>
}