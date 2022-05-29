package ro.valentin.cinemania.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.valentin.cinemania.core.Constants
import ro.valentin.cinemania.core.Constants.BASE_URL
import ro.valentin.cinemania.data.network.MovieDbApi
import ro.valentin.cinemania.data.repository.FirebaseDatabaseRepositoryImpl
import ro.valentin.cinemania.data.repository.MovieDbRepositoryImpl
import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import ro.valentin.cinemania.domain.repository.MovieDbRepository
import ro.valentin.cinemania.domain.use_case.firebase_database.FirebaseDatabaseUseCases
import ro.valentin.cinemania.domain.use_case.firebase_database.GetSeats
import ro.valentin.cinemania.domain.use_case.movieDb.GetMovieDetails
import ro.valentin.cinemania.domain.use_case.movieDb.GetMovies
import ro.valentin.cinemania.domain.use_case.movieDb.MovieDbUseCases

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
        movieDbApi: MovieDbApi,
    ): MovieDbRepository = MovieDbRepositoryImpl(
        movieDbApi = movieDbApi,
    )

    @Provides
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase = FirebaseDatabase.getInstance(Constants.FIREBASE_REAL_TIME_DATABASE_URL)

    @Provides
    fun provideFirebaseDatabaseRepository(
        firebaseDatabase: FirebaseDatabase
    ): FirebaseDatabaseRepository = FirebaseDatabaseRepositoryImpl(
        firebaseDatabase = firebaseDatabase
    )

    @Provides
    fun provideMovieDbUseCases(
        movieDbRepository: MovieDbRepository
    ) : MovieDbUseCases = MovieDbUseCases(
       getMovies = GetMovies(movieDbRepository),
        getMovieDetails = GetMovieDetails(movieDbRepository)
    )

    @Provides
    fun provideFirebaseDatabaseUseCases(
        firebaseDatabaseRepository: FirebaseDatabaseRepository
    ) : FirebaseDatabaseUseCases = FirebaseDatabaseUseCases(
        getSeats = GetSeats(firebaseDatabaseRepository)
    )
}