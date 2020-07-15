package dev.ghost.notforgotapp.entities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "priorities")
data class Priority(
    @PrimaryKey var id:Int = 0,
    var name:String = "",
    var color:String = ""
) : Parcelable {
    fun getDrawableColor(): Int
    {
        return Color.parseColor(color!!)
    }
}
