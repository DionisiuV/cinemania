package ro.valentin.cinemania.data.network.dto

import com.google.gson.annotations.SerializedName
import ro.valentin.cinemania.domain.model.Movie

data class MovieDto(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("release_date") val releaseDate: String,
    val popularity: Double,
    @SerializedName("original_language") val originalLanguage: String
)

fun MovieDto.toMovie(): Movie = Movie(id, title, posterPath, releaseDate)

