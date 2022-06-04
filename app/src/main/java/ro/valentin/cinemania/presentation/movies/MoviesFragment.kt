package ro.valentin.cinemania.presentation.movies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.data.network.dto.toMovie
import ro.valentin.cinemania.databinding.MovieDataBinding
import ro.valentin.cinemania.domain.model.Movie
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.presentation.splash.SplashActivity

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MoviesAdapter.OnMovieClickListener {
    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var movieList: List<Movie>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
        getMovies()
    }

    private fun initRecyclerView(view: View) {
        moviesRecyclerView = view.findViewById(R.id.moviesRecyclerView)

    }

    private fun initMoviesAdapter() {
        moviesAdapter = MoviesAdapter(movieList, this)
    }


    private fun getMovies() {
        viewModel.getMovies().observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Loading -> Log.d(LOG_TAG, "movies request is loading")
                is Response.Success ->{
                    movieList = response.data.movieDtoList.map { movieDto ->
                        movieDto.toMovie()
                    }
                    initMoviesAdapter()
                    setMoviesAdapter()
                }
                is Response.Error -> Log.d(LOG_TAG, response.error)
            }
        }
    }

    private fun setMoviesAdapter() {
        moviesRecyclerView.adapter = moviesAdapter
    }

    override fun onMovieClick(movie: Movie) {
        goToSplashActivity(movie.id)
    }

/*    private fun goToMovieFragment(movieId: Int) {
        val movieIdBundle = Bundle()
        movieIdBundle.putInt("movieId", movieId)
        findNavController().navigate(R.id.movieDetailsFragment, movieIdBundle)
    }*/

    private fun goToSplashActivity(movieId: Int) {
        val splashActivityIntent = Intent(context, SplashActivity::class.java)
        splashActivityIntent.putExtra("movieId", movieId)

        startActivity(splashActivityIntent)
    }
}

