package ro.valentin.cinemania.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String? = null,
    val releaseDate: String,
    val description: String,
    val rating: Double
)
