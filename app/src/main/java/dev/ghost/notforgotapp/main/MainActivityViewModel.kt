package dev.ghost.notforgotapp.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.*
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.helpers.AppDatabase
import dev.ghost.notforgotapp.helpers.LoadingState
import dev.ghost.notforgotapp.repositories.CategoryRepository
import dev.ghost.notforgotapp.repositories.PriorityRepository
import dev.ghost.notforgotapp.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val _loadingState = MutableLiveData<LoadingState>()

    private val taskRepository: TaskRepository
    private val priorityRepository: PriorityRepository
    private val categoryRepository: CategoryRepository
    private val sharedPreferences = application.getSharedPreferences("Dagger", Context.MODE_PRIVATE)

    lateinit var mainActivityAdapter: TaskAdapter

    val tasksFullInfoData: LiveData<List<TaskWithCategoryAndPriority>>
    private val categoriesData: LiveData<List<CategoryAndTasks>>
    private val prioritiesData: LiveData<List<Priority>>


    init {
        val token = sharedPreferences.getString("token", "")!!

        val appDatabase = AppDatabase.getDatabase(application)
        val apiService = ApiUtils.apiService
        taskRepository = TaskRepository(apiService, appDatabase.taskDao, token)
        priorityRepository = PriorityRepository(apiService, appDatabase.priorityDao, token)
        categoryRepository = CategoryRepository(apiService, appDatabase.categoryDao, token)

        categoriesData = categoryRepository.data
        prioritiesData = priorityRepository.data

        tasksFullInfoData = taskRepository.fullInfoData

        fetchTasks()
    }

    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    internal fun fetchTasks() {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.LOADING
                priorityRepository.refresh()
                categoryRepository.refresh()
                taskRepository.refresh()
                _loadingState.value = LoadingState.LOADED

            } catch (e: Exception) {
                _loadingState.value = LoadingState.error(e.message)
            }
        }
    }

    suspend fun removeTask(task: Task): Boolean {
        return taskRepository.deleteTask(task)
    }

    suspend fun changeTask(task: Task): Boolean {
        return taskRepository.patchTask(task)
    }

    suspend fun clearAllData() {
        taskRepository.deleteAll()
        categoryRepository.deleteAll()
        priorityRepository.deleteAll()
        sharedPreferences.edit()
            .clear().apply()
    }

}