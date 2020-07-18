package dev.ghost.notforgotapp.taskedit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityTaskEditBinding
import dev.ghost.notforgotapp.databinding.ActivityTaskInfoBinding
import dev.ghost.notforgotapp.taskinfo.TaskInfoViewModel

class TaskEditActivity : AppCompatActivity() {

    private lateinit var taskEditViewModel: TaskEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskEditViewModel = ViewModelProvider(this)
            .get(TaskEditViewModel::class.java)

//        taskInfoViewModel.currentTask =
//            (intent.getParcelableExtra(MainActivity.TASK))

        val bindingTaskInfo: ActivityTaskEditBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_task_edit)

        bindingTaskInfo.task = taskEditViewModel.currentTask
    }
}