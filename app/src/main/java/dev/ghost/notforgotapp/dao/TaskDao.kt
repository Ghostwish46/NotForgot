package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.TaskWithCategoryAndPriority
import retrofit2.http.DELETE

@Dao
interface TaskDao {
    @Transaction
    @Query("Select * from tasks")
    fun getAll():LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addList(tasks:List<Task>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(task:Task)

    @Delete
    fun delete(task:Task)

    @Transaction
    @Query("Select * from tasks")
    fun getTasksFullInfo():LiveData<List<TaskWithCategoryAndPriority>>
}