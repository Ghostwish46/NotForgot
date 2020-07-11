package dev.ghost.notforgotapp.storage

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

 class SharedPreferencesStorage @Inject constructor(context: Context) : Storage
{
    private val sharedPreferences = context.getSharedPreferences("Dagger", Context.MODE_PRIVATE)

    fun getPreferences():SharedPreferences = sharedPreferences
    fun getEditor():SharedPreferences.Editor = sharedPreferences.edit()

    override fun setString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun getString(key: String): String {
        TODO("Not yet implemented")
    }

}