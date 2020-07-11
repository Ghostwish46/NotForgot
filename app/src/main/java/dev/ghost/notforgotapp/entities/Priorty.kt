package dev.ghost.notforgotapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "priorities")
data class Priorty(
    @PrimaryKey val id:Int,
    val name:String,
    val color:String
)