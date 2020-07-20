package dev.ghost.notforgotapp.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.ghost.notforgotapp.helpers.ItemType
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    var id: Int = 0,
    var name: String = ""):ItemForList, Parcelable {
    @Ignore
    override var type: ItemType = ItemType.Category


    override fun toString(): String {
        return name
    }
}
