package ro.valentin.cinemania.domain.use_case.movie_db

data class MovieDbUseCases(
    val getMovies: GetMovies,
    val getMovieDetails: GetMovieDetails
    )