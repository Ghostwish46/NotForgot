package dev.ghost.notforgotapp.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityRegistrationBinding
import dev.ghost.notforgotapp.databinding.ActivityTaskEditBinding
import dev.ghost.notforgotapp.main.MainActivity
import dev.ghost.notforgotapp.taskedit.TaskEditViewModel
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_task_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)

        registrationViewModel = ViewModelProvider(this)
            .get(RegistrationViewModel::class.java)
        val bindingUser: ActivityRegistrationBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_registration)

        bindingUser.user = registrationViewModel.currentUser
    }

    private fun hasNoErrors(): Boolean {
        var noErrors = true
        val nameResult = registrationViewModel.checkName()
        textInputLayoutRegistrationName.error =
            if (nameResult != null) {
                textInputLayoutRegistrationName.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(nameResult)
            } else {
                textInputLayoutRegistrationName.errorIconDrawable = null
                null
            }

        val mailResult = registrationViewModel.checkMail()
        textInputLayoutRegistrationMail.error =
            if (mailResult != null) {
                textInputLayoutRegistrationMail.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(mailResult)
            } else {
                textInputLayoutRegistrationMail.errorIconDrawable = null
                null
            }

        val passwordResult = registrationViewModel.checkPassword()
        textInputLayoutRegistrationPassword.error =
            if (passwordResult != null) {
                textInputLayoutRegistrationPassword.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(passwordResult)
            } else {
                textInputLayoutRegistrationPassword.errorIconDrawable = null
                null
            }

        val differentPasswordsResult = registrationViewModel.checkDifferentPasswords()
        textInputLayoutRegistrationRePassword.error =
            if (differentPasswordsResult != null) {
                textInputLayoutRegistrationRePassword.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(differentPasswordsResult)
            } else {
                textInputLayoutRegistrationRePassword.errorIconDrawable = null
                null
            }

        return noErrors
    }

    fun registrationAction(view: View) {
        if (hasNoErrors()) {
            registrationViewModel.viewModelScope.launch {
                withContext(Dispatchers.IO)
                {
                    val registerResult = registrationViewModel.register()
                    withContext(Dispatchers.Main)
                    {
                        if (registerResult) {
                            val intentMain =
                                Intent(this@RegistrationActivity, MainActivity::class.java)
                            startActivity(intentMain)
                        } else {
                            Toast.makeText(
                                this@RegistrationActivity,
                                getString(R.string.error_connection_failed),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
        }
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}