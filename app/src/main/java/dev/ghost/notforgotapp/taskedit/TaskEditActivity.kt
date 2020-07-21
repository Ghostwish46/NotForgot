package dev.ghost.notforgotapp.taskedit

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityTaskEditBinding
import dev.ghost.notforgotapp.databinding.AlertDialogAddCategoryBinding
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priority
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority
import dev.ghost.notforgotapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_task_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class TaskEditActivity : AppCompatActivity() {

    private lateinit var taskEditViewModel: TaskEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskEditViewModel = ViewModelProvider(this)
            .get(TaskEditViewModel::class.java)

        val fullTaskInfo: TaskWithCategoryAndPriority? =
            intent.getParcelableExtra(MainActivity.TASK)

        var task: Task? = Task()
        if (fullTaskInfo != null) {
            task = fullTaskInfo.task
            task.updateEntities(fullTaskInfo.category, fullTaskInfo.priority)
        }

        taskEditViewModel.currentTask = task

        val bindingTaskInfo: ActivityTaskEditBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_task_edit)

        bindingTaskInfo.task = taskEditViewModel.currentTask

        taskEditViewModel.allCategories.observe(this, Observer {
            var categoriesAdapter: ArrayAdapter<Category> =
                ArrayAdapter<Category>(
                    this@TaskEditActivity,
                    android.R.layout.simple_spinner_item, it
                )
            categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerTaskEditCategory.adapter = categoriesAdapter

            if (it.contains(taskEditViewModel.currentTask?.category)) {
                spinnerTaskEditCategory.setSelection(
                    categoriesAdapter.getPosition(taskEditViewModel.currentTask?.category)
                )
            }
        })

        taskEditViewModel.allPriorities.observe(this, Observer {
            var prioritiesAdapter: ArrayAdapter<Priority> =
                ArrayAdapter<Priority>(
                    this@TaskEditActivity,
                    android.R.layout.simple_spinner_item, it
                )
            prioritiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerTaskEditPriority.adapter = prioritiesAdapter

            if (it.contains(taskEditViewModel.currentTask?.priority)) {
                spinnerTaskEditPriority.setSelection(
                    prioritiesAdapter.getPosition(taskEditViewModel.currentTask?.priority)
                )
            }
        })

        spinnerTaskEditCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    taskEditViewModel.currentTask?.categoryId =
                        (parent.getItemAtPosition(pos) as Category).id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }

        spinnerTaskEditPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    taskEditViewModel.currentTask?.priorityId =
                        (parent.getItemAtPosition(pos) as Priority).id
                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }

        editTextTaskEditDescription.doAfterTextChanged {
            bindingTaskInfo.invalidateAll()
        }

        editTextTaskEditEndDate.setOnClickListener {
            val calendar = GregorianCalendar()

            val dpd = DatePickerDialog(

                this@TaskEditActivity, R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val selectedDate = GregorianCalendar(year, monthOfYear, dayOfMonth)
                    taskEditViewModel.setEndDate(selectedDate.time.time / 1000)
                    bindingTaskInfo.invalidateAll()

                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }
    }

    fun editTask(view: View?) {
        if (!hasNoErrors())
        {
            return
        }

        taskEditViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                val result = taskEditViewModel.changeTask()
                withContext(Dispatchers.Main)
                {
                    if (result) {
                        Toast.makeText(
                            this@TaskEditActivity, getString(R.string.text_task_saved_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@TaskEditActivity, getString(R.string.text_api_saving_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            }
        }

    }

    fun showAddCategoryDialog(view: View) {
        val bindingAddCategory: AlertDialogAddCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.alert_dialog_add_category,
            null,
            false
        )

        bindingAddCategory.editTextAddTaskName.doAfterTextChanged {
            bindingAddCategory.invalidateAll()
        }
        bindingAddCategory.category = taskEditViewModel.categoryForAdding

        val alertDialog =
            AlertDialog.Builder(this, R.style.DialogTheme).setView(bindingAddCategory.root)
                .setPositiveButton(R.string.action_save)
                { dialog, which ->
                    taskEditViewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO)
                        {
                            val result = taskEditViewModel.addCategory()
                            withContext(Dispatchers.Main)
                            {
                                if (result) {
                                    Toast.makeText(
                                        this@TaskEditActivity,
                                        getString(R.string.text_category_saved_successfully),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@TaskEditActivity,
                                        getString(R.string.text_api_saving_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                taskEditViewModel.categoryForAdding.name = ""
                                dialog.dismiss()
                            }
                        }
                    }
                }
                .setNegativeButton(R.string.action_cancel)
                { dialog, which -> dialog.dismiss() }
                .show()
    }

    private fun hasNoErrors(): Boolean {
        var noErrors = true
        val nameResult = taskEditViewModel.checkName()
        textInputLayoutEditTaskName.error =
            if (nameResult != null)
            {
                textInputLayoutEditTaskName.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(nameResult)
            }
            else
            {
                textInputLayoutEditTaskName.errorIconDrawable = null
                null
            }

        val descriptionResult = taskEditViewModel.checkDescription()
        textInputLayoutEditTaskDescription.error =
        if (descriptionResult != null)
        {
            textInputLayoutEditTaskDescription.errorIconDrawable =
                (getDrawable(R.drawable.icon_error))
            noErrors = false
            getString(descriptionResult)
        }
        else
        {
            textInputLayoutEditTaskDescription.errorIconDrawable = null
            null
        }

        return noErrors
    }

    override fun onBackPressed() {
        if (taskEditViewModel.checkUnsavedChanges()) {
            val alertSave = AlertDialog.Builder(
                this@TaskEditActivity,
                R.style.DialogTheme
            )
                .setPositiveButton(R.string.action_yes)
                { dialog, which ->
                    editTask(null)
                }
                .setNegativeButton(R.string.action_cancel)
                { dialog, which ->
                    dialog.dismiss()
                    super.onBackPressed()
                }
                .setMessage(R.string.action_save_question)
                .show()
        } else
            super.onBackPressed()
    }
}