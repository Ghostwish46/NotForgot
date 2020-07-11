package dev.ghost.notforgotapp

import android.app.Application
import android.content.SharedPreferences
import android.os.UserManager
import dev.ghost.notforgotapp.LoginActivity_MembersInjector.create
import dev.ghost.notforgotapp.components.AppComponent


open class  App : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
//    val appComponent: AppComponent by lazy {
//        // Creates an instance of AppComponent using its Factory constructor
//        // We pass the applicationContext that will be used as Context in the graph
//        DaggerAppComponent
//            .factory()
//            .create(this)
//
//
//    }
}