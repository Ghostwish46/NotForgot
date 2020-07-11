package dev.ghost.notforgotapp.main

import androidx.lifecycle.*
import dev.ghost.notforgotapp.helpers.LoadingState
import dev.ghost.notforgotapp.repositories.CategoryRepository
import dev.ghost.notforgotapp.repositories.PriorityRepository
import dev.ghost.notforgotapp.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel (
    private val taskRepository: TaskRepository,
    private val priorityRepository: PriorityRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _loadingState = MutableLiveData<LoadingState>()

    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    val tasksData = taskRepository.data
    val categoriesData = categoryRepository.data
    val prioritiesData = priorityRepository.data

    init {
        fetchTasks()
    }

    private fun fetchTasks()
    {
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
}