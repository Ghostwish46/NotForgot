package dev.ghost.notforgotapp.storage

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(context: Context)
{
    private val sharedPreferences = context.getSharedPreferences("Dagger", Context.MODE_PRIVATE)

    fun getPreferences():SharedPreferences = sharedPreferences
    fun getEditor():SharedPreferences.Editor = sharedPreferences.edit()

}