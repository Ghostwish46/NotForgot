package dev.ghost.notforgotapp.taskinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.helpers.AppDatabase
import dev.ghost.notforgotapp.helpers.LoadingState
import dev.ghost.notforgotapp.repositories.CategoryRepository
import dev.ghost.notforgotapp.repositories.PriorityRepository
import dev.ghost.notforgotapp.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskInfoViewModel(
    application: Application
) : AndroidViewModel(application) {

    var fullTask: TaskWithCategoryAndPriority? = null
        set(value) {
            if (field != null)
                return
            else {
                field = value
            }
        }
}