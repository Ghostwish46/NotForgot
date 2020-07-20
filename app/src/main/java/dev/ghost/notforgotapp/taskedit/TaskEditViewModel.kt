package dev.ghost.notforgotapp.taskedit

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priority
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.helpers.AppDatabase
import dev.ghost.notforgotapp.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class TaskEditViewModel(
    application: Application

) : AndroidViewModel(application) {

    private val appDatabase = AppDatabase.getDatabase(application)
    private val sharedPreferences = application.getSharedPreferences("Dagger", Context.MODE_PRIVATE)
    private val taskRepository =
        TaskRepository(
            ApiUtils.apiService,
            appDatabase.taskDao,
            sharedPreferences.getString("token", "")!!
        )

    var allCategories: LiveData<List<Category>>
    var allPriorities: LiveData<List<Priority>>


    init {
        allCategories = appDatabase.categoryDao.getAll()
        allPriorities = appDatabase.priorityDao.getAll()
    }

    var currentTask: Task? = null
        set(value) {
            if (currentTask != null)
                return
            else {
                field = value
            }
        }

    fun setEndDate(value: Long) {
        currentTask?.deadline = value
    }

    fun checkTaskParams(): String {
        return ""
    }

    suspend fun changeTask(): Boolean {
        return if (currentTask?.id == 0)
            taskRepository.postTask(currentTask!!)
        else
            taskRepository.patchTask(currentTask!!)
    }
}

