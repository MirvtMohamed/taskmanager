package com.example.taskmanager.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import java.util.*
import kotlin.system.measureNanoTime

object SampleDataRunner {

    private const val TAG_DB = "DB_TEST"
    private const val TAG_DAO = "DAO_TEST"
    private const val TAG_PERF = "PERF"

    fun seedAndDemo(context: Context) {
        val db = AppDatabase.get(context)
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        scope.launch {
            // Wipe & seed for repeatable demo
            db.clearAllTables()

            // 1) Insert sample Users
            val userIds = db.userDao().insertUsers(
                User(name = "Mervat", email = "mervat@example.com"),
                User(name = "Omar", email = "omar@example.com")
            )
            Log.d(TAG_DB, "Inserted users IDs: $userIds")

            // 2) Insert Projects
            val ownerId = 1 // first user
            val pWork = Project(title = "Android App", ownerId = ownerId, labels = listOf("android","room"))
            val pDocs = Project(title = "Docs & Specs", ownerId = ownerId, labels = listOf("docs"))
            val p1Id = db.projectDao().insert(pWork).toInt()
            val p2Id = db.projectDao().insert(pDocs).toInt()
            Log.d(TAG_DB, "Inserted projects: $p1Id, $p2Id")

            // 3) Insert Tasks
            val t1Id = db.taskDao().insert(Task(description = "Design schema", projectId = p1Id, dueDate = Date())).toInt()
            val t2Id = db.taskDao().insert(Task(description = "Write DAO", projectId = p1Id, dueDate = null)).toInt()
            val t3Id = db.taskDao().insert(Task(description = "Draft README", projectId = p2Id, dueDate = Date())).toInt()
            Log.d(TAG_DB, "Inserted tasks: $t1Id, $t2Id, $t3Id")

            // 4) Add Attachments
            db.attachmentDao().insertAll(listOf(
                Attachment(filePath = "/storage/emulated/0/Android/data/app/images/schema.png", taskId = t1Id),
                Attachment(filePath = "/storage/emulated/0/Android/data/app/docs/dao.pdf", taskId = t2Id)
            ))
            Log.d(TAG_DB, "Inserted attachments for t1/t2")

            // 5) Link tasks to additional projects via many-to-many
            db.projectDao().linkTaskToProject(ProjectTaskCrossRef(projectId = p1Id, taskId = t1Id))
            db.projectDao().linkTaskToProject(ProjectTaskCrossRef(projectId = p1Id, taskId = t2Id))
            db.projectDao().linkTaskToProject(ProjectTaskCrossRef(projectId = p2Id, taskId = t3Id))
            // Cross-link: also link t3 (README) to project 1
            db.projectDao().linkTaskToProject(ProjectTaskCrossRef(projectId = p1Id, taskId = t3Id))
            Log.d(TAG_DB, "Linked tasks to projects via cross-ref")

            // 6) Query relations: ProjectWithTasks
            val p1WithTasks = db.projectDao().getProjectWithTasks(p1Id)
            Log.d(TAG_DB, "Project with Tasks: $p1WithTasks")

            // 7) Suspend vs Flow demo
            val once = db.projectDao().getAllProjectsOnce()
            Log.d(TAG_DAO, "Suspend projects: $once")

            val flowJob = launch {
                withTimeoutOrNull(3000L) {
                    db.projectDao().getAllProjectsFlow().collect { list ->
                        Log.d(TAG_DAO, "Flow emission: $list")
                    }
                }
            }
            // Trigger a change to prove flow emits
            delay(200)
            db.projectDao().insert(Project(title = "New Ideas", ownerId = ownerId, labels = listOf("brainstorm")))
            flowJob.join()

            // 8) Performance: @Query vs @RawQuery
            fun runPerf(label: String, block: suspend () -> Unit): Long = runBlocking {
                val time = measureNanoTime {
                    repeat(100) { runBlocking { block() } }
                }
                Log.d(TAG_PERF, "$label: ${time}ns for 100 runs")
                time
            }

            val timeQuery = runPerf("Room query") {
                db.projectDao().getProjectsWithMoreThanTasks(minTasks = 1)
            }
            val timeRaw = runPerf("Raw query") {
                db.projectDao().getProjectsWithMoreThanTasksRawHelper(minTasks = 1)
            }
            Log.d(TAG_PERF, "Comparison → Room: ${timeQuery}ns, Raw: ${timeRaw}ns")
        }
    }
}
