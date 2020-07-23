package dev.ghost.notforgotapp.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.R
import androidx.lifecycle.ViewModel
import dev.ghost.notforgotapp.entities.User
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val currentUser: User = User()

    private val apiService = ApiUtils.apiService
    private val sharedPreferences = application.getSharedPreferences("Dagger", Context.MODE_PRIVATE)

    // Only for testing!
    fun inflateUser() {
        currentUser.mail = "tester@gmail.com"
        currentUser.password = "qwerty1111"
    }

    suspend fun login(): Boolean {
        val loginRequest = apiService.loginPostAsync(
            currentUser.mail,
            currentUser.password
        )
        return try {
            val response = loginRequest.await()
            if (response.isSuccessful) {
                sharedPreferences.edit()
                    .putString("token", response.body()?.apiToken)
                    .apply()
                true
            } else {
                false
            }
        } catch (ex: Exception) {
            false
        }
    }

    fun getToken():String
    {
        return sharedPreferences.getString("token", "")!!
    }

    fun checkMail(): Int? {
        return if (currentUser.mail.isBlank())
            dev.ghost.notforgotapp.R.string.error_blank_mail
        else
            null
    }

    fun checkPassword(): Int? {
        return if (currentUser.password.isBlank())
            dev.ghost.notforgotapp.R.string.error_blank_password
        else
            null
    }


}
