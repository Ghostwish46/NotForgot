package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.ghost.notforgotapp.entities.Task

@Dao
interface TaskDao {
    @Transaction
    @Query("Select * from tasks")
    fun getAll():LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tasks:List<Task>)
}