package dev.ghost.notforgotapp.helpers

import dev.ghost.notforgotapp.entities.Category
import dev.ghost.notforgotapp.entities.Priorty
import dev.ghost.notforgotapp.entities.Task
import dev.ghost.notforgotapp.entities.User
import kotlinx.coroutines.Deferred
import retrofit2.Call
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
    fun getTasksAsync(@Header("api_token") token:String):Deferred<List<Task>>

    @GET("priorities")
    fun getPrioritiesAsync(@Header("api_token") token:String):Deferred<List<Priorty>>

    @GET("categories")
    fun getCategoriesAsync(@Header("api_token") token:String):Deferred<List<Category>>

}