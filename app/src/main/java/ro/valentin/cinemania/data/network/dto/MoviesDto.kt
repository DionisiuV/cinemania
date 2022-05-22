package ro.valentin.cinemania.data.network.dto

import com.google.gson.annotations.SerializedName

data class MoviesDto(
    val dates: Dates,
    val page: Int,
    @SerializedName("results") val movieDtoList: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class Dates(
    val maximum: String,
    val minimum: String
)


