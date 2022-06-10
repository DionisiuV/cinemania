package ro.valentin.cinemania.domain.repository

import kotlinx.coroutines.flow.Flow
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.model.Seat

interface FirebaseDatabaseRepository {
    suspend fun getSeats(movieId: Int, selectedLocation: String, selectedDate: String, selectedTime: String): Flow<Response<List<Seat?>>>
    suspend fun getSelectedSeatsByUser(movieId: Int, selectedLocation: String, selectedDate: String, selectedTime: String, userId: String): Flow<Response<List<String>>>
}