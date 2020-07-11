package dev.ghost.notforgotapp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.ghost.notforgotapp.components.AppComponent
import dev.ghost.notforgotapp.databinding.ActivityLoginBinding
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import dev.ghost.notforgotapp.viewmodels.RegistrationViewModel
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

    //Layout for login actions.
    private val linearLogin: LinearLayout
        get() {
            val mLinearLogin: LinearLayout = findViewById(R.id.includeLogin)
            return mLinearLogin
        }

    //Layout for Registration actions.
    private val linearRegistration: LinearLayout
        get() {
            val mLinearRegistration: LinearLayout = findViewById(R.id.includeRegistration)
            return mLinearRegistration
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)


        registrationViewModel = ViewModelProvider(this)
            .get(RegistrationViewModel::class.java)

        mApiService = ApiUtils.apiService

        val bindingUser: ActivityLoginBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        registrationViewModel.currentUser.mail = "tester@gmail.com"
        registrationViewModel.currentUser.password = "qwerty1111"

        bindingUser.user = registrationViewModel.currentUser
        bindingUser.lifecycleOwner = this

        (applicationContext as App).appComponent.injectsMainActivity(this)
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
            val loginRequest = mApiService.registrationPost(
                registrationViewModel.currentUser.mail,
                registrationViewModel.currentUser.password
            )
            try {
                val response = loginRequest.await()
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, response.body()?.apiToken,
                        LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@LoginActivity, response.errorBody()?.string(),
                        LENGTH_LONG).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@LoginActivity, ex.message, LENGTH_LONG).show();
            }
        }
    }

    // Switch visibility of login/registration layout.
    fun switchLoginRegistration(view: View) {
        linearLogin.changeVisibility()
        linearRegistration.changeVisibility()
    }

    // Actions on registration click.
    fun registrationAction(view: View) {

    }
}
