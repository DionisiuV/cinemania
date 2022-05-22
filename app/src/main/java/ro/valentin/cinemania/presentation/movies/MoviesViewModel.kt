package ro.valentin.cinemania.presentation.movies

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ro.valentin.cinemania.domain.use_case.movie_db.MovieDbUseCases
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieDbUseCases: MovieDbUseCases
) : ViewModel() {
    fun getMovies() = movieDbUseCases.getMovies()
}