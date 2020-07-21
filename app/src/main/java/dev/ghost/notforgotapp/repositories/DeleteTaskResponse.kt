package dev.ghost.notforgotapp.repositories

import com.google.gson.annotations.SerializedName

data class DeleteTaskResponse(
    val message: String,
    @SerializedName("task_id")
    val taskId: Int
) {
}