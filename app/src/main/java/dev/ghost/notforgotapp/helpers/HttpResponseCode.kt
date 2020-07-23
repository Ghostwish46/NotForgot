package dev.ghost.notforgotapp.helpers

import dev.ghost.notforgotapp.R

enum class HttpResponseCode(val code:Int) {
    CODE_EXCEPTION(-1)
    {
        override fun getErrorMessage() = R.string.code_exception
    },
    OK(200),
    BAD_REQUEST(400) {
        override fun getErrorMessage() = R.string.code_400
    },
    UNAUTHORIZED(401) {
        override fun getErrorMessage() = R.string.code_401
    },
    NOT_FOUND(404) {
        override fun getErrorMessage() = R.string.code_404
    },
    INTERNAL_SERVER_ERROR(500) {
        override fun getErrorMessage() = R.string.code_500
    };

    companion object {
        fun getByCode(code: Int) = values().find{ it.code == code }?:CODE_EXCEPTION
    }

    open fun getErrorMessage() = R.string.code_200
}