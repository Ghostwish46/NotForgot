package dev.ghost.notforgotapp.taskinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityTaskInfoBinding
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority
import dev.ghost.notforgotapp.main.MainActivity
import dev.ghost.notforgotapp.taskedit.TaskEditActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskInfoActivity : AppCompatActivity() {

    private lateinit var taskInfoViewModel: TaskInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskInfoViewModel = ViewModelProvider(this)
            .get(TaskInfoViewModel::class.java)

        val fullTaskInfo: TaskWithCategoryAndPriority = intent.getParcelableExtra(MainActivity.TASK)!!
        fullTaskInfo.task.updateEntities(fullTaskInfo.category, fullTaskInfo.priority)

        taskInfoViewModel.fullTask = fullTaskInfo

        val bindingTaskInfo: ActivityTaskInfoBinding = DataBindingUtil
                    .setContentView(this@TaskInfoActivity, R.layout.activity_task_info)
                bindingTaskInfo.task = taskInfoViewModel.fullTask?.task
    }

    fun editTask(view: View) {
        val intentEdit = Intent(this@TaskInfoActivity, TaskEditActivity::class.java)
        intentEdit.putExtra(MainActivity.TASK, taskInfoViewModel.fullTask)
        startActivity(intentEdit)
    }
}
