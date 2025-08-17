# Room Data Layer — Task Management App

This module implements a Room-backed data layer for a small team Task Management app with **Users**, **Projects**, **Tasks**, and **Attachments**.

##  Suspend DAO vs Flow DAO (Explanation)

- `suspend fun getAllProjectsOnce(): List<Project>`: runs a single query and returns a **one-time snapshot**. It will not update unless you call it again.
- `fun getAllProjectsFlow(): Flow<List<Project>>`: returns a **cold stream** that **emits whenever the underlying table changes**. Collect it to receive real-time updates without re-querying manually.

See `SampleDataRunner.seedAndDemo()` and `TaskScreen` for usage.
