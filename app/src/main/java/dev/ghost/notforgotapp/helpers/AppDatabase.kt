package dev.ghost.notforgotapp.helpers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.ghost.notforgotapp.dao.CategoryDao
import dev.ghost.notforgotapp.dao.PriorityDao
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priority
import dev.ghost.notforgotapp.entities.Task

@Database(
    entities = [Task::class, Priority::class, Category::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao:TaskDao
    abstract val priorityDao:PriorityDao
    abstract val categoryDao:CategoryDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "NotForgotDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}