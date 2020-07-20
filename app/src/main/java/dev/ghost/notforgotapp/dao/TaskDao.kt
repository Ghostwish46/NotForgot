package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority

@Dao
interface TaskDao {
    @Transaction
    @Query("Select * from tasks")
    fun getAll():LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addList(tasks:List<Task>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(task:Task)

    @Transaction
    @Query("Select * from tasks")
    fun getTasksFullInfo():LiveData<List<TaskWithCategoryAndPriority>>
}