package dev.ghost.notforgotapp.modules

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton


@Module
class StorageModule(private val context: Context) {

    @Provides
    fun providesAppContext() = context

//    @Singleton
//    @Provides
//    open fun provideSharedPreferences(): SharedPreferences {
//        return context.getSharedPreferences("NotForgot", Context.MODE_PRIVATE)
//    }
}