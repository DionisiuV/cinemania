package ro.valentin.cinemania.domain.repository

import kotlinx.coroutines.flow.Flow
import ro.valentin.cinemania.data.network.dto.MovieDto
import ro.valentin.cinemania.data.network.dto.MoviesDto
import ro.valentin.cinemania.domain.model.Response

interface MovieDbRepository {
    suspend fun getMovies(): Flow<Response<MoviesDto>>
}