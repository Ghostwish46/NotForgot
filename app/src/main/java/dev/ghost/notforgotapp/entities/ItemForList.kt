package dev.ghost.notforgotapp.entities

import android.view.View
import androidx.room.Ignore
import dev.ghost.notforgotapp.helpers.ItemType

interface ItemForList {
    var type:ItemType
}