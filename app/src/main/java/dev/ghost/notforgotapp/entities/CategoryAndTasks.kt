package dev.ghost.notforgotapp.entities

import androidx.room.Embedded
import androidx.room.Relation
import dev.ghost.notforgotapp.App
import dev.ghost.notforgotapp.helpers.AppDatabase

data class CategoryAndTasks(
    @Embedded
    val category: Category,
    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = Task::class)
    val tasks: List<Task>
)