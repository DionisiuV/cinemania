package ro.valentin.cinemania.presentation.movie_details

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import ro.valentin.cinemania.domain.model.Seat

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details), MovieDetailsAdapter.OnSeatClickListener {
    private lateinit var dataBinding: MovieDetailsDataBinding
    private lateinit var movieDetailsRecyclerView: RecyclerView
    private lateinit var movieDetailsAdapter: MovieDetailsAdapter
    private lateinit var seats: List<Seat>
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
            Seat(1, false),
            Seat(2, false),
            Seat(3, false),
            Seat(4, false),
            Seat(5, false),
            Seat(6, false),
            Seat(7, false),
            Seat(8, false),
            Seat(9, false),
            Seat(10, false),
            Seat(11, false),
            Seat(12, false),
            Seat(13, false),
            Seat(14, false),
            Seat(15, false),
            Seat(16, false),
            Seat(17, false),
            Seat(18, false),
            Seat(19, false),
            Seat(20, false),
            Seat(21, false),
            Seat(22, false),
            Seat(23, false),
            Seat(24, false),
            Seat(25, false),
            Seat(26, false),
            Seat(27, false),
            Seat(28, false),
            Seat(29, false),
            Seat(30, false),
        )
        movieDetailsAdapter = MovieDetailsAdapter(seats, this)
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

    override fun onSeatClick(seat: Seat, position: Int) {
        Toast.makeText(context, "${seat.number} ${seat.available}", Toast.LENGTH_SHORT).show()
        seat.available = !seat.available
        if(seat.available) {
            movieDetailsRecyclerView.layoutManager?.findViewByPosition(position)?.setBackgroundColor(
                Color.parseColor("#FF0000"))
        } else {
            movieDetailsRecyclerView.layoutManager?.findViewByPosition(position)?.setBackgroundColor(
                Color.parseColor("#000000"))
        }
    }
}