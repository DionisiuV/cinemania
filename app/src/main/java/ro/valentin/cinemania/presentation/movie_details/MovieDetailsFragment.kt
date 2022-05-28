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
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.BR
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.data.network.dto.toMovie
import ro.valentin.cinemania.databinding.MovieDetailsDataBinding
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.model.Seat
import ro.valentin.cinemania.domain.model.getSeats
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details), MovieDetailsAdapter.OnSeatClickListener {
    private lateinit var dataBinding: MovieDetailsDataBinding
    private lateinit var movieDetailsRecyclerView: RecyclerView
    private lateinit var movieDetailsAdapter: MovieDetailsAdapter
    private var seats = getSeats()
    private var movieId: Int? = null
    @Inject
    lateinit var dbRef: FirebaseDatabase
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
        movieId = getMovieIdFromBundle()


        //todo, implement lastUpdate: sessionId and update on click only if sessionId from last update == sessionId from user
//        val randomUUID = UUID.randomUUID().toString()
//        dbRef.getReference("seats").child(movieId.toString()).child("sessionID").setValue(randomUUID)

        dbRef.getReference("seats").child(movieId.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fetchSeatsDetails()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        initRecyclerView(view)
        initMovieDetailsAdapter()
        setMoviesAdapter()
        getMovieDetails(movieId)
        fetchSeatsDetails()
    }

    private fun initRecyclerView(view: View) {
        movieDetailsRecyclerView = view.findViewById(R.id.seatsRecyclerView)
        movieDetailsRecyclerView.layoutManager = GridLayoutManager(view.context, 6)
    }

   private fun initMovieDetailsAdapter() {
        Log.d(LOG_TAG, "init moviedetails adapter")
        movieDetailsAdapter = MovieDetailsAdapter(seats, this)
    }

    private fun setMoviesAdapter() {
        movieDetailsRecyclerView.adapter = movieDetailsAdapter
    }


    private fun getMovieIdFromBundle() = arguments?.getInt("movieId")

    private fun getMovieDetails(movieId: Int?) {


        if (movieId != null) {
            viewModel.getMovieDetail(movieId).observe(viewLifecycleOwner) { response ->
                when(response) {
                    is Response.Loading -> Log.d(LOG_TAG, "movie data is loading")
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
        if(!seats[position].available) {
            Toast.makeText(context, "Not available", Toast.LENGTH_LONG).show()
        } else {
            seats[position].available = !seats[position].available
            updateSeats(seats[position])
        }

    }

    private fun updateSeats(seat: Seat) {
        dbRef.getReference("seats").child(movieId.toString()).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    Log.d(LOG_TAG, "data exists, need to update")
                    val map = mutableMapOf<String, Any>()
                    map["available"] = seat.available
                    dbRef.getReference("seats").child(movieId.toString()).child(seat.number!!).updateChildren(map)
                    Log.d(LOG_TAG, "Data updated, need to update UI")
                } else {
                    Log.d(LOG_TAG, "no data, need to save it")
                    dbRef.getReference("seats").child(movieId.toString()).setValue(seats)
                    Log.d(LOG_TAG, "Data saved, need to update UI")
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, "error: ${error.message}")
            }
        })
    }

    private fun fetchSeatsDetails() {
        Log.d(LOG_TAG, movieId.toString())

        if(view != null) {
            movieId?.let { viewModel.getSeats(it) }?.observe(viewLifecycleOwner) { response ->
                when(response) {
                    is Response.Loading -> Log.d(LOG_TAG, "is loading from rtdb")
                    is Response.Success ->{

                        if(response.data.isNotEmpty()) {
                            seats.clear()
                            seats.addAll(response.data as MutableList<Seat>)

                            movieDetailsAdapter.notifyDataSetChanged()
                        }
                    }
                    is Response.Error -> Log.d(LOG_TAG, response.error)
                }
            }
        }
        }



}