package ro.valentin.cinemania.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.valentin.cinemania.core.Constants.BASE_URL
import ro.valentin.cinemania.data.network.MovieDbApi
import ro.valentin.cinemania.data.repository.MovieDbRepositoryImpl
import ro.valentin.cinemania.domain.repository.MovieDbRepository
import ro.valentin.cinemania.domain.use_case.movie_db.GetMovieDetails
import ro.valentin.cinemania.domain.use_case.movie_db.GetMovies
import ro.valentin.cinemania.domain.use_case.movie_db.MovieDbUseCases

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideMovieDbApi(): MovieDbApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(MovieDbApi::class.java)

    @Provides
    fun provideMovieRepository(
        movieDbApi: MovieDbApi
    ): MovieDbRepository = MovieDbRepositoryImpl(
        movieDbApi = movieDbApi
    )

    @Provides
    fun provideMovieDbUseCases(
        movieDbRepository: MovieDbRepository
    ) : MovieDbUseCases = MovieDbUseCases(
       getMovies = GetMovies(movieDbRepository),
        getMovieDetails = GetMovieDetails(movieDbRepository)
    )
}