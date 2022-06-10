package ro.valentin.cinemania.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import ro.valentin.cinemania.domain.use_case.firebase_auth.FirebaseAuthUseCases
import ro.valentin.cinemania.domain.use_case.firebase_database.FirebaseDatabaseUseCases
import ro.valentin.cinemania.domain.use_case.movieDb.MovieDbUseCases
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDbUseCases: MovieDbUseCases,
    private val firebaseDatabaseUseCases: FirebaseDatabaseUseCases,
    private val firebaseAuthUseCases: FirebaseAuthUseCases
) : ViewModel(){

    fun getMovieDetail(movieId: Int) = liveData(Dispatchers.IO) {
        movieDbUseCases.getMovieDetails(movieId).collect {
            emit(it)
        }
    }

    fun getSeats(
        movieId: Int,
        selectedLocation: String,
        selectedDate: String,
        selectedTime: String
    ) = liveData(Dispatchers.IO) {
        firebaseDatabaseUseCases.getSeats(movieId, selectedLocation, selectedDate, selectedTime).collect {
            emit(it)
        }
    }

    fun getCurrentUser() = firebaseAuthUseCases.getCurrentUser()

    fun getSelectedSeatsByUser(
        movieId: Int,
        selectedLocation: String,
        selectedDate: String,
        selectedTime: String,
        userId: String
    ) = liveData(Dispatchers.IO) {
        firebaseDatabaseUseCases.getSelectedSeatsByUser(movieId, selectedLocation, selectedDate, selectedTime, userId).collect{
            emit(it)
        }
    }
}