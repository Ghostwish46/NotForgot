package dev.ghost.notforgotapp.viewmodels

import androidx.lifecycle.ViewModel
import dev.ghost.notforgotapp.entities.User

class RegistrationViewModel : ViewModel() {
    val currentUser:User  = User()

}
