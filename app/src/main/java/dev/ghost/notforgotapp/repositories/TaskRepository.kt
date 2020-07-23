package dev.ghost.notforgotapp.repositories

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.EntityState
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.HttpResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class TaskRepository(
    private val apiService: ApiService,
    private val taskDao: TaskDao,
    private val token: String
) {

    val data = taskDao.getAll()
    val fullInfoData = taskDao.getTasksFullInfo()

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val tasks = apiService.getTasksAsync(token).await()
            tasks.forEach { task ->
                try {
                    task.updateKeys()
                    taskDao.add(task)
                } catch (ex: Exception) {
                    Log.e("TASK DB ERROR", ex.message + " " + task.toString())
                }
            }
        }
    }

    suspend fun postTask(task: Task): HttpResponseCode {
        return withContext(Dispatchers.IO)
        {
            try {
                val taskRequest = apiService
                    .addTaskAsync(token, task)
                val response = taskRequest.await()
                if (response.isSuccessful) {
                    val newTask = response.body()!!
                    newTask.updateKeys()
                    taskDao.add(newTask)
                } else {
                    task.entityState = EntityState.ADDED
                    taskDao.add(task)
                }
                HttpResponseCode.getByCode(response.code())
            } catch (ex: Exception) {
                task.entityState = EntityState.ADDED
                taskDao.add(task)
                HttpResponseCode.getByCode(-1)
            }
        }
    }

    suspend fun patchTask(task: Task): HttpResponseCode {
        return withContext(Dispatchers.IO)
        {
            try {
                val taskRequest = apiService
                    .patchTaskAsync(task.id, token, task)
                val response = taskRequest.await()
                if (response.isSuccessful) {
                    val changedTask = response.body()!!
                    changedTask.updateKeys()
                    taskDao.add(changedTask)
                } else {
                    if (task.entityState != EntityState.ADDED)
                        task.entityState = EntityState.MODIFIED
                    taskDao.add(task)
                }
                HttpResponseCode.getByCode(response.code())
            } catch (ex: Exception) {
                if (task.entityState != EntityState.ADDED)
                    task.entityState = EntityState.MODIFIED
                taskDao.add(task)
                HttpResponseCode.getByCode(-1)
            }
        }
    }

    suspend fun deleteTask(task: Task): HttpResponseCode {
        return withContext(Dispatchers.IO)
        {
            val taskRequest = apiService
                .deleteTaskAsync(task.id, token)
            try {
                val response = taskRequest.await()
                if (response.isSuccessful) {
                    taskDao.delete(task)
                } else {
                    task.entityState = EntityState.DELETED
                    taskDao.add(task)
                }
                HttpResponseCode.getByCode(response.code())

            } catch (ex: Exception) {
                task.entityState = EntityState.DELETED
                taskDao.add(task)
                HttpResponseCode.getByCode(-1)
            }
        }
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }
}