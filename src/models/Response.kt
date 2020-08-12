package wcode.software.models

data class Response(
    val code: Int,
    val message: String? = null
)