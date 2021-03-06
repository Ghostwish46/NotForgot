package dev.ghost.notforgotapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.CategoryAndTasks

@Dao
interface CategoryDao {
    @Query("Select * from categories")
    fun getAll(): LiveData<List<Category>>


    @Query("Select * from categories where id = :id")
    fun getById(id:Int): Category

    @Transaction
    @Query("Select * from categories")
    fun getCategoriesWithTasks():LiveData<List<CategoryAndTasks>>

    @Query("Delete from categories")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMany(categories:List<Category>)
}