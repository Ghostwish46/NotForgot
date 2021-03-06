package dev.ghost.notforgotapp.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.ghost.notforgotapp.login.LoginActivity
import dev.ghost.notforgotapp.main.MainActivity
import dev.ghost.notforgotapp.modules.StorageModule
import javax.inject.Singleton

// NOT IMPLEMENTED!
@Singleton
@Component(modules = [StorageModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context)
                : AppComponent
    }


    fun injectsLoginActivity(loginActivity: LoginActivity)

    fun injectsMainActivity(mainActivity: MainActivity)
}