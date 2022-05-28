package ro.valentin.cinemania.domain.use_case.firebase_database

import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import javax.inject.Inject

class GetSeats @Inject constructor(
   private val firebaseDatabaseRepository: FirebaseDatabaseRepository
){

    suspend operator fun invoke(movieId: Int) = firebaseDatabaseRepository.getSeats(movieId)
}