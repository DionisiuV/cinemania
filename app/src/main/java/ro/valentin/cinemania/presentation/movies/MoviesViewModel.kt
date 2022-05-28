package ro.valentin.cinemania.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import ro.valentin.cinemania.domain.use_case.movieDb.MovieDbUseCases
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieDbUseCases: MovieDbUseCases
) : ViewModel() {
    fun getMovies() = liveData(Dispatchers.IO) {
        movieDbUseCases.getMovies().collectLatest {
            emit(it)
        }
    }
}