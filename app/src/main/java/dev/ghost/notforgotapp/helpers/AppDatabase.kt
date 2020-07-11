package dev.ghost.notforgotapp.helpers

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.ghost.notforgotapp.dao.CategoryDao
import dev.ghost.notforgotapp.dao.PriorityDao
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priorty
import dev.ghost.notforgotapp.entities.Task

@Database(
    entities = [Task::class, Priorty::class, Category::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao:TaskDao
    abstract val priorityDao:PriorityDao
    abstract val categoryDao:CategoryDao
}