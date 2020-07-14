package dev.ghost.notforgotapp.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.ghost.notforgotapp.helpers.ItemType

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    var id: Int = 0,
    var name: String = ""):ItemForList {
    @Ignore
    override var type: ItemType = ItemType.Category
}
