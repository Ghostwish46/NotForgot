package dev.ghost.notforgotapp.entities


import android.view.View
import androidx.room.*
import com.google.gson.annotations.SerializedName
import dev.ghost.notforgotapp.helpers.ItemType

@Entity(
    tableName = "tasks", foreignKeys = [ForeignKey(
        entity = Priority::class,
        parentColumns = ["id"],
        childColumns = ["priorityId"]
    ), ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"]
    )]
        , indices = [Index("categoryId"), Index("priorityId")])
data class Task(
    @PrimaryKey val id: Int = 0,
    var title: String = "",
    var description: String = "",
    var done: Int = 0,
    var deadline: Long = 0,
    var created: Long = 0
) :ItemForList{
    @Ignore
    override var type: ItemType = ItemType.Task

    @Ignore
    @SerializedName("category")
    var category: Category? = null

    @Ignore
    @SerializedName("priority")
    var priority: Priority? = null

    var categoryId: Int = 0
    var priorityId: Int = 0


    fun updateKeys(){
        categoryId = category?.id ?: 0
        priorityId = priority?.id ?: 0
    }

    fun updateEntities(category: Category, priority: Priority)
    {
        this.category = category
        this.priority = priority
    }


}