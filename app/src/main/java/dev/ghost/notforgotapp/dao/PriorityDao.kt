package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ghost.notforgotapp.entities.Priorty

@Dao
interface PriorityDao {
    @Query("Select * from priorities")
    fun getAll(): LiveData<List<Priorty>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tasks:List<Priorty>)
}