package dev.ghost.notforgotapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dev.ghost.notforgotapp.databinding.ActivityLoginBinding
import dev.ghost.notforgotapp.entities.User
import dev.ghost.notforgotapp.helpers.ApiService
import dev.ghost.notforgotapp.helpers.ApiUtils
import dev.ghost.notforgotapp.viewmodels.RegistrationViewModel
import kotlinx.android.synthetic.main.layout_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    //Variable declaration
    var mApiService: ApiService? = null

    //    lateinit var  mLinearLogin:LinearLayout
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)

        registrationViewModel = ViewModelProvider(this)
            .get(RegistrationViewModel::class.java)
//        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)


        val bindingUser: ActivityLoginBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        registrationViewModel.currentUser.mail = "tester@gmail.com"
        registrationViewModel.currentUser.password = "qwerty1111"

        bindingUser.user = registrationViewModel.currentUser
        bindingUser.lifecycleOwner = this

        val mLinearLogin: LinearLayout = findViewById(R.id.includeLogin)
        val mLinearRegistration: LinearLayout = findViewById(R.id.includeRegistration)


        // Switch registration and login layouts.
        val btnRegistration: Button = mLinearLogin.findViewById(R.id.buttonLoginRegistration)
        btnRegistration.setOnClickListener {
            Toast.makeText(this, registrationViewModel.currentUser.mail, LENGTH_LONG).show()
            mLinearLogin.changeVisibility()
            mLinearRegistration.changeVisibility()
        }

        val btnLogin: Button = mLinearRegistration.findViewById(R.id.buttonRegistrationLogin)
        btnLogin.setOnClickListener {
            mLinearLogin.changeVisibility()
            mLinearRegistration.changeVisibility()
        }


        val btnLoginAction: Button = mLinearLogin.findViewById(R.id.buttonLoginAction)
        btnLoginAction.setOnClickListener {
            mApiService!!.registrationPost(
                registrationViewModel.currentUser.mail,
                registrationViewModel.currentUser.password
            ).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {


                    if (response.isSuccessful()) {
                        Toast.makeText(this@LoginActivity, response.body()?.apiToken, LENGTH_LONG)
                            .show();
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.errorBody()?.string(),
                            LENGTH_LONG
                        ).show();
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, LENGTH_LONG).show();
                }
            })
        }

        mApiService = ApiUtils.apiService


    }

    // Function for view's visibility changing.
    private fun View.changeVisibility() {
        if (visibility == View.VISIBLE) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
        }
    }
}
