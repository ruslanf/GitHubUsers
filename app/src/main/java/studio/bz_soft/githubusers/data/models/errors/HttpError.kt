package studio.bz_soft.githubusers.data.models.errors

import java.lang.RuntimeException

class HttpError(error: HttpErrorBody): RuntimeException(error.message)

data class HttpErrorBody(
    val code: Int,
    val message: String,
    val response: String
)