package dev.ghost.notforgotapp.login

import androidx.lifecycle.R
import androidx.lifecycle.ViewModel
import dev.ghost.notforgotapp.entities.User

class LoginViewModel : ViewModel() {
    val currentUser: User = User()

    // Only for testing!
    fun inflateUser() {
        currentUser.mail = "tester@gmail.com"
        currentUser.password = "qwerty1111"
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
