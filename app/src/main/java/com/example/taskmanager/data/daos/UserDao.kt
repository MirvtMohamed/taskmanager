package com.example.taskmanager.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmanager.data.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUsers(vararg user: User): List<Long>

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>
}