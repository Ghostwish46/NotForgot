package dev.ghost.notforgotapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ghost.notforgotapp.dao.CategoryDao
import dev.ghost.notforgotapp.entities.CategoryAndTasks
import dev.ghost.notforgotapp.helpers.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val token: String
) {
    var data:LiveData<List<CategoryAndTasks>> =categoryDao.getCategoriesWithTasks()

    suspend fun refresh() {
        withContext(Dispatchers.IO)
        {
            val categories = apiService.getCategoriesAsync(token)
                .await()
            categoryDao.add(categories)
        }
    }
}