package dev.ghost.notforgotapp.helpers

data class LoadingState private constructor(
    val status: Status,
    val message: String? = null
) {
    companion object
    {
        val LOADED = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.RUNNING)
        fun error(msg:String?) = LoadingState(Status.FAILED, msg)
    }
}