package dev.ghost.notforgotapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "priorities")
data class Priority(
    @PrimaryKey var id:Int = 0,
    var name:String = "",
    var color:String = ""
)