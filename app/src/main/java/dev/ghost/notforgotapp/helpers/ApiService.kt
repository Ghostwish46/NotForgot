package dev.ghost.notforgotapp.helpers

import dev.ghost.notforgotapp.entities.User
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun registrationPost(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<Response<User>>


}