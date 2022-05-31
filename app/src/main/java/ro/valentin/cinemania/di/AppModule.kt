package ro.valentin.cinemania.di

import android.app.Application
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants
import ro.valentin.cinemania.core.Constants.BASE_URL
import ro.valentin.cinemania.data.network.MovieDbApi
import ro.valentin.cinemania.data.repository.AuthRepositoryImpl
import ro.valentin.cinemania.data.repository.FirebaseDatabaseRepositoryImpl
import ro.valentin.cinemania.data.repository.MovieDbRepositoryImpl
import ro.valentin.cinemania.domain.repository.AuthRepository
import ro.valentin.cinemania.domain.repository.FirebaseDatabaseRepository
import ro.valentin.cinemania.domain.repository.MovieDbRepository
import ro.valentin.cinemania.domain.use_case.firebase_database.FirebaseDatabaseUseCases
import ro.valentin.cinemania.domain.use_case.firebase_database.GetSeats
import ro.valentin.cinemania.domain.use_case.movieDb.GetMovieDetails
import ro.valentin.cinemania.domain.use_case.movieDb.GetMovies
import ro.valentin.cinemania.domain.use_case.movieDb.MovieDbUseCases
import javax.inject.Named

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

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideOneTapClient(application: Application):
            SignInClient = Identity.getSignInClient(
        application.applicationContext
    )

    @Provides
    @Named("signUpRequest")
    fun provideSignUpRequest(application: Application): BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(application.getString(R.string.web_client_id))
                // Show all accounts on the device.
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

    @Provides
    @Named("signInRequest")
    fun provideSignInRequest(application: Application): BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(application.getString(R.string.web_client_id))
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build())
        .build()

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named("signInRequest") beginSignInRequest: BeginSignInRequest,
        @Named("signUpRequest") beginSignUpRequest: BeginSignInRequest
    ): AuthRepository = AuthRepositoryImpl(
        firebaseAuth = firebaseAuth,
        oneTapClient = oneTapClient,
        beginSignInRequest = beginSignInRequest,
        beginSignUpRequest = beginSignUpRequest
    )
}