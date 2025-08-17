package com.example.taskmanager.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.taskmanager.data.entities.Attachment

@Dao
interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(attachment: Attachment): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(attachments: List<Attachment>): List<Long>
}