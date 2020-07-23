package dev.ghost.notforgotapp.helpers

import androidx.room.TypeConverter
import dev.ghost.notforgotapp.entities.EntityState

class StateConverter {
    @TypeConverter
    fun toEntityState(value: Int) = enumValues<EntityState>()[value]

    @TypeConverter
    fun fromEntityState(value: EntityState) = value.ordinal
}