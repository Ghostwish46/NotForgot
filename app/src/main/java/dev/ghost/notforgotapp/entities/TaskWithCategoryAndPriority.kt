package dev.ghost.notforgotapp.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import dev.ghost.notforgotapp.helpers.ItemType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskWithCategoryAndPriority(
    @Embedded
    val task: Task,

    @Relation(parentColumn = "priorityId", entityColumn = "id", entity = Priority::class)
    val priority: Priority,

    @Relation(parentColumn = "categoryId", entityColumn = "id", entity = Category::class)
    val category: Category
):ItemForList, Parcelable
{
    @Ignore
    override var type: ItemType = ItemType.Task
}