package ro.valentin.cinemania.data.repository

import kotlinx.coroutines.flow.flow
import retrofit2.await
import ro.valentin.cinemania.core.Constants.API_KEY_VALUE
import ro.valentin.cinemania.data.network.MovieDbApi
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.repository.MovieDbRepository
import javax.inject.Inject

class MovieDbRepositoryImpl @Inject constructor(
    private val movieDbApi: MovieDbApi
) : MovieDbRepository {
    override suspend fun getMovies() = flow {
        try {
            emit(Response.Loading)
            val result = movieDbApi.getMovies(API_KEY_VALUE).await()
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }
}