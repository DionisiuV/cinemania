package ro.valentin.cinemania.domain.repository

import kotlinx.coroutines.flow.Flow
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.model.Seat

interface FirebaseDatabaseRepository {
    suspend fun getSeats(movieId: Int): Flow<Response<List<Seat?>>>
    suspend fun getSelectedSeatsByUser(movieId: Int, userId: String): Flow<Response<List<String>>>
}