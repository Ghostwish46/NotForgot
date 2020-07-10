package dev.ghost.notforgotapp.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

class User() {
    var mail:String = ""
    var name:String = ""
    var password:String = ""
    var repassword:String = ""
    @SerializedName("api_token")
    var apiToken:String = ""
}