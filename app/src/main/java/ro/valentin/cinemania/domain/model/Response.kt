package ro.valentin.cinemania.domain.model

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val error: String) : Response<Nothing>()
    object Loading : Response<Nothing>()
}