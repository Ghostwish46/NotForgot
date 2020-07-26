package dev.ghost.notforgotapp.helpers

object ApiUtils {

    private const val BASE_URL = "http://practice.mobile.kreosoft.ru/api/"

    val apiService: ApiService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(ApiService::class.java)

}