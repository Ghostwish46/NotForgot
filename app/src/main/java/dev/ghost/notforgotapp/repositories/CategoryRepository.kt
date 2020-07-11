package dev.ghost.notforgotapp.repositories

import dev.ghost.notforgotapp.dao.CategoryDao
import dev.ghost.notforgotapp.helpers.ApiService
import javax.inject.Inject

class CategoryRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val token: String
) {
    val data = categoryDao.getAll()

    suspend fun refresh()
    {
        val categories = apiService.getCategoriesAsync(token).await()
        categoryDao.add(categories)
    }
}