package ro.valentin.cinemania.presentation.movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

    }
}