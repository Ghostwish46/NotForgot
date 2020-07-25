package dev.ghost.notforgotapp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityLoginBinding
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.main.MainActivity
import dev.ghost.notforgotapp.registration.RegistrationActivity
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    //ViewModel instance.
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)

        loginViewModel = ViewModelProvider(this)
            .get(LoginViewModel::class.java)

        if (loginViewModel.getToken().isNotEmpty()) {
            val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentMain)
            finish()
        }


        val bindingUser: ActivityLoginBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        // Fill test data.
        loginViewModel.inflateUser()

        bindingUser.user = loginViewModel.currentUser
        bindingUser.lifecycleOwner = this
    }

    private fun hasNoErrors(): Boolean {
        var noErrors = true

        val mailResult = loginViewModel.checkMail()
        textInputLayoutLoginMail.error =
            if (mailResult != null) {
                textInputLayoutLoginMail.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(mailResult)
            } else {
                textInputLayoutLoginMail.errorIconDrawable = null
                null
            }

        val passwordResult = loginViewModel.checkPassword()
        textInputLayoutLoginPassword.error =
            if (passwordResult != null) {
                textInputLayoutLoginPassword.errorIconDrawable =
                    (getDrawable(R.drawable.icon_error))
                noErrors = false
                getString(passwordResult)
            } else {
                textInputLayoutLoginPassword.errorIconDrawable = null
                null
            }
        return noErrors
    }


    // Actions on login click.
    fun loginAction(view: View) {
        if (hasNoErrors()) {
            loginViewModel.viewModelScope.launch {
                withContext(Dispatchers.IO)
                {
                    val loginResult = loginViewModel.login()
                    withContext(Dispatchers.Main)
                    {
                        if (loginResult) {
                            val intentMain =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intentMain)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
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


    fun goToRegistration(view: View) {
        val intentRegistration = Intent(this, RegistrationActivity::class.java)
        startActivity(intentRegistration)
    }
}
