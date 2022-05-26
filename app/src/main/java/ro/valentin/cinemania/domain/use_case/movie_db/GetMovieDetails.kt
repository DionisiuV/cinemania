package ro.valentin.cinemania.domain.use_case.movie_db

import ro.valentin.cinemania.domain.repository.MovieDbRepository
import javax.inject.Inject

class GetMovieDetails @Inject constructor(
    private val movieDbRepository: MovieDbRepository
) {
    suspend operator fun invoke(movieId: Int) = movieDbRepository.getMovieDetails(movieId)
}