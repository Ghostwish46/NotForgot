package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ghost.notforgotapp.entities.Task

@Dao
interface TaskDao {
    @Query("Select * from tasks")
    fun getAll():LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tasks:List<Task>)
}