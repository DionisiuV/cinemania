package ro.valentin.cinemania.domain.use_case.movieDb

import ro.valentin.cinemania.domain.repository.MovieDbRepository
import javax.inject.Inject

class GetMovies @Inject constructor(
    private val movieDbRepository: MovieDbRepository
) {
    suspend operator fun invoke() = movieDbRepository.getMovies()
}