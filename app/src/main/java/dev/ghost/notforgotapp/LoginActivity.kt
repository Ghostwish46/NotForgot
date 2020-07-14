package dev.ghost.notforgotapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.ghost.notforgotapp.databinding.ActivityLoginBinding
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.main.MainActivity
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import dev.ghost.notforgotapp.viewmodels.RegistrationViewModel
import kotlinx.android.synthetic.main.activity_login.*
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
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)




        registrationViewModel = ViewModelProvider(this)
            .get(RegistrationViewModel::class.java)

        mApiService = ApiUtils.apiService

        val bindingUser: ActivityLoginBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        // Fill test data.
        registrationViewModel.inflateUser()

        bindingUser.user = registrationViewModel.currentUser
        bindingUser.lifecycleOwner = this

        (applicationContext as App).appComponent.injectsLoginActivity(this)

        if (sharedPreferences.getPreferences()
                .getString("token", "")?.isNotEmpty()!!
        ) {
            val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentMain)
        }

    }

    // Function for view's visibility changing.
    private fun View.changeVisibility() {
        if (visibility == View.VISIBLE) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
        }
    }

    // Actions on login click.
    fun loginAction(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            val loginRequest = mApiService.registrationPostAsync(
                registrationViewModel.currentUser.mail,
                registrationViewModel.currentUser.password
            )
            try {
                val response = loginRequest.await()
                if (response.isSuccessful) {
//                    Toast.makeText(this@LoginActivity, response.body()?.apiToken,
//                        LENGTH_LONG).show()
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

    // Switch visibility of login/registration layout.
    fun switchLoginRegistration(view: View) {
        includeLogin.changeVisibility()
        includeRegistration.changeVisibility()
    }

    // Actions on registration click.
    fun registrationAction(view: View) {

    }
}
