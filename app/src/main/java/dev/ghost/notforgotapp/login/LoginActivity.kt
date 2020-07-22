package dev.ghost.notforgotapp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.R
import dev.ghost.notforgotapp.databinding.ActivityLoginBinding
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.main.MainActivity
import dev.ghost.notforgotapp.registration.RegistrationActivity
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesStorage

    //ApiService instance.
    lateinit var mApiService: ApiService

    //ViewModel instance.
    private lateinit var registrationViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)

        (applicationContext as App).appComponent.injectsLoginActivity(this)

        if (sharedPreferences.getPreferences()
                .getString("token", "")?.isNotEmpty()!!
        ) {
            val intentMain = Intent(this@LoginActivity, MainActivity::class.java)

            startActivity(intentMain)
        }

        registrationViewModel = ViewModelProvider(this)
            .get(LoginViewModel::class.java)

        mApiService = ApiUtils.apiService

        val bindingUser: ActivityLoginBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        // Fill test data.
        registrationViewModel.inflateUser()

        bindingUser.user = registrationViewModel.currentUser
        bindingUser.lifecycleOwner = this
    }


    // Actions on login click.
    fun loginAction(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            val loginRequest = mApiService.loginPostAsync(
                registrationViewModel.currentUser.mail,
                registrationViewModel.currentUser.password
            )
            try {
                val response = loginRequest.await()
                if (response.isSuccessful) {
                    sharedPreferences.getEditor()
                        .putString("token", response.body()?.apiToken)
                        .apply()
                    val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intentMain)
                } else {
                    Toast.makeText(
                        this@LoginActivity, response.errorBody()?.string(),
                        LENGTH_LONG
                    ).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@LoginActivity, ex.message, LENGTH_LONG).show();
            }
        }
    }



    fun goToRegistration(view: View) {
        val intentRegistration = Intent(this, RegistrationActivity::class.java)
        intentRegistration.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
        startActivity(intentRegistration)
    }
}
