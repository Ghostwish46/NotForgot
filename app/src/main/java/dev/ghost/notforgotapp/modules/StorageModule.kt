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
public class StorageModule {

    private var context: Context? = null

    open fun storageModule(context: Context?) {
        this.context = context
    }

    @Singleton
    @Provides
    open fun provideSharedPrefences(): SharedPreferences {
        return context!!.getSharedPreferences("NotForgot", Context.MODE_PRIVATE)
    }
}