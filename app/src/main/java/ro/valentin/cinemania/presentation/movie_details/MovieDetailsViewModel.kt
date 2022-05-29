package ro.valentin.cinemania.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ro.valentin.cinemania.domain.use_case.firebase_database.FirebaseDatabaseUseCases
import ro.valentin.cinemania.domain.use_case.movieDb.MovieDbUseCases
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDbUseCases: MovieDbUseCases,
    private val firebaseDatabaseUseCases: FirebaseDatabaseUseCases
) : ViewModel(){

    fun getMovieDetail(movieId: Int) = liveData(Dispatchers.IO) {
        movieDbUseCases.getMovieDetails(movieId).collect {
            emit(it)
        }
    }

    fun getSeats(movieId: Int) = liveData(Dispatchers.IO) {
        firebaseDatabaseUseCases.getSeats(movieId).collect {
            emit(it)
        }
    }
}