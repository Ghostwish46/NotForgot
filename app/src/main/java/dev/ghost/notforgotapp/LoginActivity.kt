package dev.ghost.notforgotapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import dev.ghost.notforgotapp.databinding.ActivityLoginBinding
import dev.ghost.notforgotapp.databinding.LayoutLoginBinding
import dev.ghost.notforgotapp.databinding.LayoutRegistrationBinding
import dev.ghost.notforgotapp.models.User


class LoginActivity : AppCompatActivity() {

//    lateinit var  mLinearLogin:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)


        val currentUser: User = User()
        currentUser.name = "Meow!!"

        val bindingUser:ActivityLoginBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)
        bindingUser.user = currentUser



        val mLinearLogin:LinearLayout = findViewById(R.id.includeLogin)
        val mLinearRegistration:LinearLayout = findViewById(R.id.includeRegistration)


        // Switch registration and login layouts.
        val btnRegistration:Button = mLinearLogin.findViewById(R.id.buttonLoginRegistration)
        btnRegistration.setOnClickListener {
            mLinearLogin.changeVisibility()
            mLinearRegistration.changeVisibility()
        }

        val btnLogin:Button = mLinearRegistration.findViewById(R.id.buttonRegistrationLogin)
        btnLogin.setOnClickListener {
            mLinearLogin.changeVisibility()
            mLinearRegistration.changeVisibility()
        }



//        val bindingUser:ActivityLoginBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_login)
//        bindingUser.user = currentUser


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
