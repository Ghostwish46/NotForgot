package dev.ghost.notforgotapp.taskinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityTaskInfoBinding
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.main.MainActivity

class TaskInfoActivity : AppCompatActivity() {

    private lateinit var taskInfoViewModel: TaskInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskInfoViewModel = ViewModelProvider(this)
            .get(TaskInfoViewModel::class.java)

        val currentTask: Task = intent.getParcelableExtra(MainActivity.TASK)!!
        currentTask.updateEntities(intent.getParcelableExtra(MainActivity.CATEGORY)!!,
                intent.getParcelableExtra(MainActivity.PRIORITY)!!)
        taskInfoViewModel.currentTask = currentTask


        val bindingTaskInfo: ActivityTaskInfoBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_task_info)

        bindingTaskInfo.task = taskInfoViewModel.currentTask


    }
}
