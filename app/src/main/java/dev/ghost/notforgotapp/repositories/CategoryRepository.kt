package dev.ghost.notforgotapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ghost.notforgotapp.dao.CategoryDao
import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.CategoryAndTasks
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.HttpResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CategoryRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val token: String
) {
    var data: LiveData<List<CategoryAndTasks>> = categoryDao.getCategoriesWithTasks()

    suspend fun refresh() {
        withContext(Dispatchers.IO)
        {
            val categories = apiService.getCategoriesAsync(token)
                .await()
            categoryDao.addMany(categories)
        }
    }

    suspend fun postCategory(category: Category): HttpResponseCode {
        return withContext(Dispatchers.IO)
        {
            try {
                val categoryRequest = apiService
                    .addCategoryAsync(token, category)
                val response = categoryRequest.await()
                if (response.isSuccessful) {
                    val newCategory = response.body()!!
                    categoryDao.add(newCategory)
                }
                HttpResponseCode.getByCode(response.code())
            } catch (ex: Exception) {
                HttpResponseCode.getByCode(-1)
            }
        }
    }

    suspend fun deleteAll() {
        categoryDao.deleteAll()
    }
}