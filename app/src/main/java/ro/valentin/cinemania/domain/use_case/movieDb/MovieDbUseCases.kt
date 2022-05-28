package ro.valentin.cinemania.domain.use_case.movieDb

data class MovieDbUseCases(
    val getMovies: GetMovies,
    val getMovieDetails: GetMovieDetails
    )