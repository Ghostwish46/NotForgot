package dev.ghost.notforgotapp.repositories

import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.helpers.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TaskRepository(
    private val apiService: ApiService,
    private val taskDao: TaskDao,
    private val token: String
) {
    val data = taskDao.getAll()

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val tasks = apiService.getTasksAsync(token).await()
            taskDao.add(tasks)
        }
    }
}