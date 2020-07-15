package dev.ghost.notforgotapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.helpers.*
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object
    {
        const val TASK = "task"
        const val PRIORITY = "priority"
        const val CATEGORY = "category"
    }
    @Inject
    lateinit var sharedPreferences: SharedPreferencesStorage

    //    @Inject
    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        (application as App).appComponent.injectsMainActivity(this)

        mainActivityViewModel = MainActivityViewModel(
            applicationContext as App, sharedPreferences.getPreferences()
                .getString("token", "")!!
        )

        val taskAdapter = TaskAdapter(this)
        recyclerMainTasks.adapter = taskAdapter
        recyclerMainTasks.layoutManager = LinearLayoutManager(this)

//        mainActivityViewModel.prioritiesData.observe(this, Observer {
//            it.forEach { priority ->
//                Toast.makeText(this, priority.name, Toast.LENGTH_SHORT).show()
//            }
//        })
//
        mainActivityViewModel.categoriesData.observe(this, Observer {
            it.forEach {
                it.tasks.forEach {
                    it.updateEntities(database.categoryDao.getById(it.categoryId),
                        database.priorityDao.getById(it.priorityId))
                }
            }
            taskAdapter.updateData(it)
        })
//
//        mainActivityViewModel.tasksData.observe(this, Observer {
//            it.forEach {
//                if (it.category == null)
//                {
//                    it.category = mainActivityViewModel.
//            }
//              taskAdapter.updateTasks(it)
//            }
//        })

        mainActivityViewModel.loadingState.observe(this, Observer {
            when (it.status) {
                Status.FAILED -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                Status.RUNNING -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                Status.SUCCESS -> Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
        })



    }
}
