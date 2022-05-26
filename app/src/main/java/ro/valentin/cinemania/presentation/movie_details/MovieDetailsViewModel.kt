package ro.valentin.cinemania.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import ro.valentin.cinemania.domain.repository.MovieDbRepository
import ro.valentin.cinemania.domain.use_case.movie_db.MovieDbUseCases
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDbUseCases: MovieDbUseCases
) : ViewModel(){
    fun getMovieDetail(movieId: Int) = liveData(Dispatchers.IO) {
        movieDbUseCases.getMovieDetails(movieId).collect {
            emit(it)
        }
    }
}