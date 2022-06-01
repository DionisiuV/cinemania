package ro.valentin.cinemania.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants
import ro.valentin.cinemania.presentation.auth.AuthActivity
import ro.valentin.cinemania.presentation.movie_details.MovieDetailsActivity
import ro.valentin.cinemania.presentation.movie_details.MovieDetailsFragment
import ro.valentin.cinemania.presentation.movies.MoviesFragment
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity: AppCompatActivity(R.layout.activity_splash) {
    private val splashViewModel: SplashViewModel by viewModels()
    private var idMovie: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idMovie = intent.extras?.getInt("movieId")!!

        checkIfUserIsAuthenticated()
    }

    private fun checkIfUserIsAuthenticated() {
        if(splashViewModel.isUserAuthenticated) {
            Timer().schedule(1000) {
                goToMovieDetails(idMovie)
            }
        } else {
            Timer().schedule(1000) {
                goToAuthActivity(idMovie)
            }
        }
    }

    private fun goToMovieDetails(movieId: Int) {
        val movieDetailsIntent = Intent(this, MovieDetailsActivity::class.java)
        movieDetailsIntent.putExtra("movieId", movieId)

        Log.d(Constants.LOG_TAG, "from goToMovieDetails() movieId = $movieId")

        startActivity(movieDetailsIntent)
        finish()
    }


    private fun goToAuthActivity(movieId: Int) {
        val authIntent = Intent(this, AuthActivity::class.java)
       authIntent.putExtra("movieId", movieId)

        Log.d(Constants.LOG_TAG, "from goToAuthActivity() movieId = $movieId")

        startActivity(authIntent)
        finish()
    }


}