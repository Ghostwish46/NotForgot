package dev.ghost.notforgotapp.helpers

import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priority
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.User
import dev.ghost.notforgotapp.repositories.DeleteTaskResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun registrationPostAsync(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<Response<User>>

    @GET("tasks")
    @Headers("Accept:application/json")
    fun getTasksAsync(@Query("api_token") token: String): Deferred<List<Task>>

    @GET("priorities")
    @Headers("Accept:application/json")
    fun getPrioritiesAsync(@Query("api_token") token: String): Deferred<List<Priority>>

    @GET("categories")
    @Headers("Accept:application/json")
    fun getCategoriesAsync(@Query("api_token") token: String): Deferred<List<Category>>

    @POST("tasks")
    fun addTaskAsync(
        @Query("api_token") token: String,
        @Body task: Task
    ): Deferred<Response<Task>>

    @PATCH("tasks/{id}")
    @Headers("Accept:application/json")
    fun patchTaskAsync(
        @Path("id") id:Int,
        @Query("api_token") token: String,
        @Body task: Task
    ): Deferred<Response<Task>>

    @DELETE("tasks/{id}")
    @Headers("Accept:application/json")
    fun deleteTaskAsync(
        @Path("id") id:Int,
        @Query("api_token") token: String
    ): Deferred<Response<DeleteTaskResponse>>

    @POST("categories")
    fun addCategoryAsync(
        @Query("api_token") token: String,
        @Body category: Category
    ): Deferred<Response<Category>>

}