package ro.valentin.cinemania.domain.use_case.firebase_database

import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import javax.inject.Inject

class GetSelectedSeatsByUser @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
){

    suspend operator fun invoke(
        movieId: Int,
        selectedLocation: String,
        selectedDate: String, 
        selectedTime: String,
        userId: String
    ) = firebaseDatabaseRepository.getSelectedSeatsByUser(movieId, selectedLocation, selectedDate, selectedTime, userId)
}