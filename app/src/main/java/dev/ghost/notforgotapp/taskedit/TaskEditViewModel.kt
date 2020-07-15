package dev.ghost.notforgotapp.taskedit

import androidx.lifecycle.ViewModel
import dev.ghost.notforgotapp.entities.Task

class TaskEditViewModel(

) : ViewModel() {

    var currentTask:Task? = null
        set(value) {
            if (currentTask == null)
                currentTask = value
        }
}