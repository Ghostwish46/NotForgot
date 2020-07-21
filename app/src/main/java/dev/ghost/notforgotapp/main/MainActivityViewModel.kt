package dev.ghost.notforgotapp.main

import android.app.Application
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
    application: Application,
    token: String
) : AndroidViewModel(application) {
    private val _loadingState = MutableLiveData<LoadingState>()

    private val taskRepository: TaskRepository
    private val priorityRepository: PriorityRepository
    private val categoryRepository: CategoryRepository

    val tasksFullInfoData: LiveData<List<TaskWithCategoryAndPriority>>
    private val categoriesData: LiveData<List<CategoryAndTasks>>
    private val prioritiesData: LiveData<List<Priority>>


    init {
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

    suspend fun removeTask(task:Task): Boolean {
        return taskRepository.deleteTask(task)
    }
}