package dev.ghost.notforgotapp.entities

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: Int,
    val title:String,
    val description:String,
    val done:Int,
    val deadline:Long,
    val category: Category,
    val priority:Priorty,
    val created:Long
)