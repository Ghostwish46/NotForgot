package dev.ghost.notforgotapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.helpers.LoadingState
import dev.ghost.notforgotapp.helpers.Status
import java.time.Duration
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

//    @Inject
    lateinit var mainActivityViewModel:MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        (application as App).appComponent.injectsMainActivity(this)

        mainActivityViewModel.prioritiesData.observe(this, Observer {
            it.forEach { priority ->
                Toast.makeText(this, priority.name, Toast.LENGTH_SHORT).show()
            }
        })

        mainActivityViewModel.categoriesData.observe(this, Observer {
            it.forEach { category ->
                Toast.makeText(this, category.name, Toast.LENGTH_SHORT).show()
            }
        })

        mainActivityViewModel.tasksData.observe(this, Observer {
            it.forEach { task ->
                Toast.makeText(this, task.title, Toast.LENGTH_SHORT).show()
            }
        })

        mainActivityViewModel.loadingState.observe(this, Observer {
            when (it.status) {
                Status.FAILED -> Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
                Status.RUNNING -> Toast.makeText(baseContext, "Loading", Toast.LENGTH_SHORT).show()
                Status.SUCCESS -> Toast.makeText(baseContext, "Success", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
