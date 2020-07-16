package dev.ghost.notforgotapp.taskinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityTaskInfoBinding
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskInfoActivity : AppCompatActivity() {

    private lateinit var taskInfoViewModel: TaskInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskInfoViewModel = ViewModelProvider(this)
            .get(TaskInfoViewModel::class.java)

        val currentTask: Task = intent.getParcelableExtra(MainActivity.TASK)!!

        taskInfoViewModel.currentTask = currentTask
        taskInfoViewModel.viewModelScope.launch(Dispatchers.IO) {
            taskInfoViewModel.loadEntities()

            launch(Dispatchers.Main) {
                val bindingTaskInfo: ActivityTaskInfoBinding = DataBindingUtil
                    .setContentView(this@TaskInfoActivity, R.layout.activity_task_info)
                bindingTaskInfo.task = taskInfoViewModel.currentTask
            }
        }






    }
}
