package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ghost.notforgotapp.entities.Category

@Dao
interface CategoryDao {
    @Query("Select * from categories")
    fun getAll(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tasks:List<Category>)
}