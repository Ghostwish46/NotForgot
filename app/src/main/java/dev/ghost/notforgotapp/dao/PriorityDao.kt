package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ghost.notforgotapp.entities.Priority

@Dao
interface PriorityDao {
    @Query("Select * from priorities")
    fun getAll(): LiveData<List<Priority>>

    @Query("Select * from priorities where id = :id")
    fun getById(id:Int): Priority


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tasks:List<Priority>)
}