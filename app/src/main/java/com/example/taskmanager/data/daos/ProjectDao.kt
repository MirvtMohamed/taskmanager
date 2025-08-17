package com.example.taskmanager.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.taskmanager.data.entities.Project
import com.example.taskmanager.data.entities.ProjectTaskCrossRef
import com.example.taskmanager.data.entities.ProjectWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(project: Project): Long

    // 2.4 Suspend vs Flow
    @Query("SELECT * From projects ORDER By id ASC")
    suspend fun getAllProjectsOnce(): List<Project>

    @Query("SELECT * From projects ORDER By id ASC")
    fun getAllProjectsFlow(): Flow<List<Project>>

    // 2.3 Relation example
    // 2.3 Relation example
    @Transaction
    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectWithTasks(projectId: Int): List<ProjectWithTasks>

    // Link/unlink for cross-ref
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun linkTaskToProject(crossRef: ProjectTaskCrossRef): Long

    @Query("DELETE FROM project_task_cross_ref WHERE projectId = :projectId AND taskId = :taskId")
    suspend fun unlinkTaskFromProject(projectId: Int, taskId: Int): Int

    // 2.6 "Projects with more than N tasks" using @Query (JOIN + GROUP BY over cross-ref)
    @Transaction
    @Query("""
        SELECT p.* FROM projects p
        JOIN project_task_cross_ref x ON x.projectId = p.id
        GROUP BY p.id
        HAVING COUNT(x.taskId) > :minTasks
    """)
    suspend fun getProjectsWithMoreThanTasks(minTasks: Int): List<Project>

    // 2.6 same using @RawQuery
    @RawQuery
    suspend fun getProjectsWithMoreThanTasksRaw(query: SupportSQLiteQuery): List<Project>

    suspend fun getProjectsWithMoreThanTasksRawHelper(minTasks: Int): List<Project> {
        val q = SimpleSQLiteQuery(
            """
            SELECT p.* FROM projects p
            JOIN project_task_cross_ref x ON x.projectId = p.id
            GROUP BY p.id
            HAVING COUNT(x.taskId) > ?
        """.trimIndent(), arrayOf(minTasks)
        )
        return getProjectsWithMoreThanTasksRaw(q)
    }
}