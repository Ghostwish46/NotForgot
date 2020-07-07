package dev.ghost.notforgotapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class LoginActivity : AppCompatActivity() {

//    lateinit var  mLinearLogin:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)
        setContentView(R.layout.activity_login)

        val mLinearLogin:LinearLayout = findViewById(R.id.includeLogin)
        val mLinearRegistration:LinearLayout = findViewById(R.id.includeRegistration)


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


    }

    private fun View.changeVisibility() {
        if (visibility == View.VISIBLE) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
        }
    }
}
