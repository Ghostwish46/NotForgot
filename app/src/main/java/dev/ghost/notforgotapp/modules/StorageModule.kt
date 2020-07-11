package dev.ghost.notforgotapp.modules

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.ghost.notforgotapp.storage.SharedPreferencesStorage
import dev.ghost.notforgotapp.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton


@Module
abstract class StorageModule {

    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage



}