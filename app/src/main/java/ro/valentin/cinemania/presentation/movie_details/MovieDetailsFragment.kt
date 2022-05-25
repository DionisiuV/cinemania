package ro.valentin.cinemania.presentation.movie_details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initMoviesAdapter()
//        setMoviesAdapter()

    }

    private fun getMovieIdFromBundle() = arguments?.getInt("movieId")

    private fun getMovieDetails() {
        val movieId = getMovieIdFromBundle()
    }
}