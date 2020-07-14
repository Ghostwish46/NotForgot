package dev.ghost.notforgotapp.repositories

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.helpers.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TaskRepository(
    private val apiService: ApiService,
    private val taskDao: TaskDao,
    private val token: String
) {

    val data = taskDao.getAll()

//    suspend fun getAllData(): MutableLiveData<List<Task>> {
//        val taskData = taskDao.getAll()
//        taskData.observe(LifecycleOwner {  })
//        withContext(Dispatchers.IO)
//        {
//
//        }
//    }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val tasks = apiService.getTasksAsync(token).await()
            tasks.forEach{task -> task.updateKeys()}

            taskDao.add(tasks)
        }
    }
}