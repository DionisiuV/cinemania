package ro.valentin.cinemania.domain.use_case.movie_db

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import ro.valentin.cinemania.domain.repository.MovieDbRepository
import javax.inject.Inject

class GetMovies @Inject constructor(
    private val movieDbRepository: MovieDbRepository
) {
    suspend operator fun invoke() = movieDbRepository.getMovies()
}