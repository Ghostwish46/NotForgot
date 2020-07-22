package dev.ghost.notforgotapp.taskedit

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priority
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.helpers.AppDatabase
import dev.ghost.notforgotapp.repositories.CategoryRepository
import dev.ghost.notforgotapp.repositories.TaskRepository

class TaskEditViewModel(
    application: Application

) : AndroidViewModel(application) {

    private val appDatabase = AppDatabase.getDatabase(application)
    private val token = application
        .getSharedPreferences("Dagger", Context.MODE_PRIVATE)
        .getString("token", "")!!
    private val taskRepository =
        TaskRepository(
            ApiUtils.apiService,
            appDatabase.taskDao,
            token
        )
    private val categoryRepository =
        CategoryRepository(
            ApiUtils.apiService,
            appDatabase.categoryDao,
            token
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

    var categoryForAdding: Category = Category(0, "")

    fun setEndDate(value: Long) {
        currentTask?.deadline = value
    }

    fun checkUnsavedChanges(): Boolean {
        return !currentTask?.title.isNullOrEmpty()
                && !currentTask?.description.isNullOrEmpty()
    }

    fun checkName(): Int?
    {
        return if (currentTask?.title.isNullOrBlank())
            R.string.error_blank_task_title
        else
            null
    }

    fun checkDescription(): Int?
    {
        return if (currentTask?.description.isNullOrBlank())
            R.string.error_blank_task_description
        else
            null
    }

    suspend fun changeTask(): Boolean {
        return if (currentTask?.id == 0)
            taskRepository.postTask(currentTask!!)
        else
            taskRepository.patchTask(currentTask!!)
    }

    suspend fun addCategory(): Boolean {
        return categoryRepository.postCategory(categoryForAdding)
    }
}

