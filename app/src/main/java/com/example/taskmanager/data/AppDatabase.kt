package com.example.taskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanager.data.converters.DateConverter
import com.example.taskmanager.data.converters.ListStringConverter
import com.example.taskmanager.data.daos.AttachmentDao
import com.example.taskmanager.data.daos.ProjectDao
import com.example.taskmanager.data.daos.TaskDao
import com.example.taskmanager.data.daos.UserDao
import com.example.taskmanager.data.entities.Attachment
import com.example.taskmanager.data.entities.Project
import com.example.taskmanager.data.entities.ProjectTaskCrossRef
import com.example.taskmanager.data.entities.Task
import com.example.taskmanager.data.entities.User

@Database(
    entities = [
        User::class,
        Project::class,
        Task::class,
        Attachment::class,
        ProjectTaskCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun attachmentDao(): AttachmentDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_manager.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}