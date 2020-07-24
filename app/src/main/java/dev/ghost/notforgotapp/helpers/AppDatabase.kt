package dev.ghost.notforgotapp.helpers

import android.content.Context
import androidx.arch.core.util.Function
import androidx.databinding.adapters.Converters
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ghost.notforgotapp.dao.CategoryDao
import dev.ghost.notforgotapp.dao.PriorityDao
import dev.ghost.notforgotapp.dao.TaskDao
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.CategoryAndTasks
import dev.ghost.notforgotapp.entities.Priority
import dev.ghost.notforgotapp.entities.Task
import java.time.OffsetTime


@Database(
    entities = [Task::class, Priority::class, Category::class],
    version = 2, exportSchema = false
)
@TypeConverters(StateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val priorityDao: PriorityDao
    abstract val categoryDao: CategoryDao


//    open fun getInfoAboutAll(): LiveData<List<CategoryAndTasks>> {
//        var categoriesLiveData: LiveData<List<CategoryAndTasks>> =
//            categoryDao.getCategoriesWithTasks()
//        categoriesLiveData = Transformations.map(
//            categoriesLiveData, Function<List<CategoryAndTasks>, List<CategoryAndTasks>> {
//                @Override
//                fun apply(val inputCategories:List<CategoryAndTasks>):List<CategoryAndTasks>
//                {
//                    for (category in inputCategories) {
//                        category.tasks.forEach {
//                            it.updateEntities(categoryDao.getById(it.categoryId),
//                            priorityDao.getById(it.priorityId))
//                        }
//                    }
//                    return inputCategories!!
//                }
//            })
//            return categoriesLiveData
//    }


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
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}