package ro.valentin.cinemania.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ro.valentin.cinemania.core.Constants.LATEST_MOVIES_URI
import ro.valentin.cinemania.data.network.dto.MoviesDto

interface MovieDbApi {
    @GET(LATEST_MOVIES_URI)
    fun getMovies(
        @Query("api_key") apiKey: String
    ): Call<MoviesDto>
}