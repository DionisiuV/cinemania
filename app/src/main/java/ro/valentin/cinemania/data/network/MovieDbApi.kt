package ro.valentin.cinemania.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ro.valentin.cinemania.core.Constants.MOVIE_DETAILS_URI
import ro.valentin.cinemania.core.Constants.NOW_PLAYING_URI
import ro.valentin.cinemania.data.network.dto.MovieDetailsDto
import ro.valentin.cinemania.data.network.dto.MoviesDto

interface MovieDbApi {
    @GET(NOW_PLAYING_URI)
    fun getMovies(
        @Query("api_key") apiKey: String
    ): Call<MoviesDto>

    @GET("$MOVIE_DETAILS_URI{movieId}")
    fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ) : Call<MovieDetailsDto>
}