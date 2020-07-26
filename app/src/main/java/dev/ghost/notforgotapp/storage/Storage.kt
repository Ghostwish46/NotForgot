package dev.ghost.notforgotapp.storage

// NOT IMPLEMENTED!
interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String
}