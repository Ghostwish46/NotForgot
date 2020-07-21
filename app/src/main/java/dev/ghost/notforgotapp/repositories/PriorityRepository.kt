package dev.ghost.notforgotapp.repositories

import dev.ghost.notforgotapp.dao.PriorityDao
import dev.ghost.notforgotapp.helpers.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PriorityRepository(
    private val apiService: ApiService,
    private val priorityDao: PriorityDao,
    private val token: String
) {
    val data = priorityDao.getAll()

    suspend fun refresh() {
        withContext(Dispatchers.IO)
        {
            val priorities = apiService.getPrioritiesAsync(token)
                .await()
            priorityDao.add(priorities)
        }
    }

    suspend fun deleteAll()
    {
        priorityDao.deleteAll()
    }
}