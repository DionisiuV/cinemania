package ro.valentin.cinemania.presentation.movie_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.BR
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.data.network.dto.toMovie
import ro.valentin.cinemania.databinding.MovieDetailsDataBinding
import ro.valentin.cinemania.domain.model.Response

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private lateinit var dataBinding: MovieDetailsDataBinding
    private lateinit var movieDetailsRecyclerView: RecyclerView
    private lateinit var movieDetailsAdapter: MovieDetailsAdapter
    private lateinit var seats: List<String>
    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = MovieDetailsDataBinding.inflate(inflater, container, false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
        initMovieDetailsAdapter()
        setMoviesAdapter()
        getMovieDetails()
    }

    private fun initRecyclerView(view: View) {
        movieDetailsRecyclerView = view.findViewById(R.id.seatsRecyclerView)
        movieDetailsRecyclerView.layoutManager = GridLayoutManager(view.context, 6)



    }

    private fun initMovieDetailsAdapter() {
        seats = listOf(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "13",
                "14",
                "15",
                "16",
                "17",
                "18",
                "19",
                "20",
                "21",
                "22",
                "23",
                "24",
                "25",
                "26",
                "27",
                "28",
                "29",
                "30",
            )
        movieDetailsAdapter = MovieDetailsAdapter(seats)
    }

    private fun setMoviesAdapter() {
        movieDetailsRecyclerView.adapter = movieDetailsAdapter
    }


    private fun getMovieIdFromBundle() = arguments?.getInt("movieId")

    private fun getMovieDetails() {
        val movieId = getMovieIdFromBundle()

        if (movieId != null) {
            viewModel.getMovieDetail(movieId).observe(viewLifecycleOwner) { response ->
                when(response) {
                    is Response.Loading -> Log.d(LOG_TAG, "is loading")
                    is Response.Success ->{
                        val movie = response.data.toMovie()
                        dataBinding.setVariable(BR.movie, movie)
                    }
                    is Response.Error -> Log.d(LOG_TAG, response.error)
                }

            }
        }
    }
}