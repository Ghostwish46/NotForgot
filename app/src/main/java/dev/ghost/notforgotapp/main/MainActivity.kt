package dev.ghost.notforgotapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority
import dev.ghost.notforgotapp.helpers.Status
import dev.ghost.notforgotapp.login.LoginActivity
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import dev.ghost.notforgotapp.taskedit.TaskEditActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        const val TASK = "task"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferencesStorage

    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        (application as App).appComponent.injectsMainActivity(this)

        mainActivityViewModel = MainActivityViewModel(
            applicationContext as App
        )

        mainActivityViewModel.mainActivityAdapter = TaskAdapter(this, mainActivityViewModel)
        recyclerMainTasks.adapter = mainActivityViewModel.mainActivityAdapter
        recyclerMainTasks.layoutManager = LinearLayoutManager(this)

        val taskTouchHelperCallBack =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item =
                        (mainActivityViewModel.mainActivityAdapter.getItemByViewHolder(viewHolder))

                    if (item is TaskWithCategoryAndPriority)
                        mainActivityViewModel.viewModelScope
                            .launch {
                                if (mainActivityViewModel.removeTask(item.task)) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.text_task_deleted),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Connection error?
                                }
                            }
                }

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    if (viewHolder.itemViewType == 1) {
                        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        return ItemTouchHelper.Callback.makeMovementFlags(
                            dragFlags,
                            swipeFlags
                        )
                    } else {
                        val dragFlags = 0
                        val swipeFlags = 0
                        return ItemTouchHelper.Callback.makeMovementFlags(
                            dragFlags,
                            swipeFlags
                        )
                    }
                }
            }

        val taskTouchHelper = ItemTouchHelper(taskTouchHelperCallBack)
        taskTouchHelper.attachToRecyclerView(recyclerMainTasks)


        mainActivityViewModel.tasksFullInfoData.observe(this, Observer {
            includeNoTasks.visibility =
                if (it.isEmpty())
                    View.VISIBLE
                else
                    View.INVISIBLE

            mainActivityViewModel.mainActivityAdapter.updateData(it)
        })

        mainActivityViewModel.loadingState.observe(this, Observer {
            when (it.status) {
                Status.RUNNING -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()

                else -> {
                    swipeRefreshMain.isRefreshing = false
                }
            }
        })

        swipeRefreshMain.setOnRefreshListener {
            mainActivityViewModel.fetchTasks()
        }
    }

    fun addNewTaskTransition(v: View) {
        val intentNewTask = Intent(this, TaskEditActivity::class.java)
        startActivity(intentNewTask)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                mainActivityViewModel.viewModelScope.launch {
                    withContext(Dispatchers.IO)
                    {
                        mainActivityViewModel.clearAllData()
                    }
                    withContext(Dispatchers.Main)
                    {
                        val intentLogin = Intent(this@MainActivity, LoginActivity::class.java)
                        intentLogin.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(intentLogin)
                    }
                }

            }
        }
        return true
    }
}
