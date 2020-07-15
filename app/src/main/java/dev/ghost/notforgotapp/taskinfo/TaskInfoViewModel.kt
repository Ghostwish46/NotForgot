package dev.ghost.notforgotapp.taskinfo

import androidx.lifecycle.ViewModel
import dev.ghost.notforgotapp.entities.Task

class TaskInfoViewModel(

) : ViewModel() {

    var currentTask:Task? = null
        set(value) {
            if (currentTask != null)
                return
            else
            {
                field = value
            }
        }
}