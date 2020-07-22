package dev.ghost.notforgotapp.registration

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import dev.ghost.notforgotapp.entities.User
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.main.MainActivity

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    val currentUser:User = User()

    private val apiService = ApiUtils.apiService
    private val sharedPreferences = application.getSharedPreferences("Dagger", Context.MODE_PRIVATE)

    suspend fun register():Boolean
    {
        val loginRequest = apiService.registerPostAsync(
            currentUser.mail,
            currentUser.name,
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

    fun checkName(): Int? {
        return if (currentUser.name.isBlank())
            dev.ghost.notforgotapp.R.string.error_blank_name
        else
            null
    }

    fun checkDifferentPasswords(): Int? {
        return if (currentUser.password != currentUser.repassword)
            dev.ghost.notforgotapp.R.string.error_different_passwords
        else
            null
    }
}
