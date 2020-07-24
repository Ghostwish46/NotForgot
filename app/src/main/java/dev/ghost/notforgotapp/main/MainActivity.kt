package dev.ghost.notforgotapp.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority
import dev.ghost.notforgotapp.helpers.AppDatabase
import dev.ghost.notforgotapp.helpers.HttpResponseCode
import dev.ghost.notforgotapp.helpers.Status
import dev.ghost.notforgotapp.login.LoginActivity
import dev.ghost.notforgotapp.login.LoginViewModel
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

    lateinit var connectivityManager: ConnectivityManager
    lateinit var networkCallback: NetworkCallback

    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        mainActivityViewModel = ViewModelProvider(this)
            .get(MainActivityViewModel(application)::class.java)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                syncTasks()
            }

            override fun onLost(network: Network) {
            }
        }


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
                                val result = mainActivityViewModel.removeTask(item.task)
                                if (result == HttpResponseCode.OK) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.text_task_deleted),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(result.getErrorMessage()) + " "
                                                + getString(R.string.text_task_deleted_locally),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                }

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return if (viewHolder.itemViewType == 1) {
                        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        ItemTouchHelper.Callback.makeMovementFlags(
                            dragFlags,
                            swipeFlags
                        )
                    } else {
                        val dragFlags = 0
                        val swipeFlags = 0
                        ItemTouchHelper.Callback.makeMovementFlags(
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
        mainActivityViewModel.unSynchronizedTasks.observe(this, Observer {  })

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

    override fun onResume() {
        super.onResume()
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun syncTasks() {

        mainActivityViewModel.viewModelScope.launch {
            withContext(Dispatchers.Main)
            {
                val animationView = View.inflate(this@MainActivity, R.layout.alert_sync_tasks, null)
                val alertDialog =
                    AlertDialog.Builder(this@MainActivity, R.style.DialogTheme)
                        .setView(animationView)
                        .setMessage(R.string.text_tasks_syncing)
                        .setCancelable(true)
                        .show()

                withContext(Dispatchers.IO)
                {
                    mainActivityViewModel.syncTasks()
                }

                alertDialog.dismiss()
            }
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
