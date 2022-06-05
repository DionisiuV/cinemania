package ro.valentin.cinemania.presentation.movie_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.BR
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.core.Utils.Companion.hide
import ro.valentin.cinemania.core.Utils.Companion.show
import ro.valentin.cinemania.data.network.dto.toMovie
import ro.valentin.cinemania.databinding.MovieDetailsDataBinding
import ro.valentin.cinemania.domain.model.Movie
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.domain.model.Seat
import ro.valentin.cinemania.domain.model.getSeats
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details),
    MovieDetailsAdapter.OnSeatClickListener {
    private val viewModel by viewModels<MovieDetailsViewModel>()
    @Inject
    lateinit var dbRef: FirebaseDatabase
    private lateinit var dataBinding: MovieDetailsDataBinding
    private lateinit var movieDetailsAdapter: MovieDetailsAdapter
    private lateinit var seats: MutableList<Seat>
    private var movieId: Int? = 0
    private lateinit var user: FirebaseUser
    private lateinit var nextButton: Button
    private lateinit var loaderProgressBar: ProgressBar
    private lateinit var movie: Movie

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
        seats = getSeats()
        movieId = getMovieIdFromIntent()
        user = getCurrentUser()!!
        nextButton = view.findViewById(R.id.nextButton)

            Log.d(LOG_TAG, user.email.toString())

        initProgressBar(view)

        //if currentUser does not exists in db add It
        checkAndAddUserToDb()

        //fetch movie details from api
        getMovieDetails(movieId)

        //check if movie exist in db, if not add and create list of seat
        fetchSeatsDetails()

        //find recyclerView and attach adapter
        setRecyclerView(view)

        initNextButton()

    }

    private fun setRecyclerView(view: View) {
        val movieDetailsRecyclerView: RecyclerView = view.findViewById(R.id.seatsRecyclerView)
        val query: Query = dbRef.getReference("seats").child(movieId.toString())
        val firebaseRecyclerOptions: FirebaseRecyclerOptions<Seat> =
            FirebaseRecyclerOptions.Builder<Seat>()
                .setQuery(query, Seat::class.java)
                .build()

        movieDetailsAdapter = MovieDetailsAdapter(this, firebaseRecyclerOptions)

        movieDetailsRecyclerView.layoutManager = GridLayoutManager(view.context, 6)
        movieDetailsRecyclerView.adapter = movieDetailsAdapter
    }

    private fun getMovieIdFromIntent() = activity?.intent?.getIntExtra("movieId", 0)

    private fun getMovieDetails(movieId: Int?) {
        if (movieId != null) {
            viewModel.getMovieDetail(movieId).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        loaderProgressBar.show()
                        Log.d(LOG_TAG, "movie data is loading")
                    }
                    is Response.Success -> {
                        movie = response.data.toMovie()
                        dataBinding.setVariable(BR.movie, movie)
                        loaderProgressBar.hide()

                    }
                    is Response.Error -> Log.d(LOG_TAG, response.error)
                }
            }
        }
    }

    //on seat click check if is available or not
    //if it's available, mark as unavailable and update lastUpdate field with user id
    //if it's unavailable and only if lastUpdate field == userId mark as available, otherwise display err msg
    override fun onSeatClick(seat: Seat, position: Int) {
        if (!seat.available) {
            //seat unavailable, check if lastUpdate field from DB == currentUser id
            //if yes, mark as available
            Log.d(LOG_TAG, "onSeatClick() ${user.uid}")
            dbRef.getReference("seats").child(movieId.toString()).child(seat.number.toString()).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val lastUpdateIdFromDb = snapshot.child("lastUpdate").value.toString()

                    if(user.uid == lastUpdateIdFromDb) {
                        seat.available = !seat.available
                        updateSeat(seat)
                    } else {
                        Toast.makeText(context, "Already taken", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   Log.d(LOG_TAG, error.message)
                }
            })

            //if not

        } else {
            //seat available, mark as unavailable
            seat.available = !seat.available
            updateSeat(seat)
        }
    }

    private fun updateSeat(seat: Seat) {
        val map = mutableMapOf<String, Any>()
        map["available"] = seat.available
        map["lastUpdate"] = user.uid
        dbRef.getReference("seats").child(movieId.toString()).child(seat.number!!)
            .updateChildren(map)
    }

    private fun fetchSeatsDetails() {
        if (view != null) {
            movieId?.let { viewModel.getSeats(it) }?.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        //show progressBar
                        Log.d(LOG_TAG, "getSeatDetails() request is loading")
                        loaderProgressBar.show()
                    }
                    is Response.Success -> {
                        if (response.data.isNotEmpty()) {
                            //hide progressBar
                            Log.d(LOG_TAG, "data is there")
                        } else {
                            Log.d(LOG_TAG, "data is not there, need to push it")
                            dbRef.getReference("seats").child(movieId.toString()).setValue(seats)
                            Log.d(LOG_TAG, "data pushed")
                            //hide progressBar
                            loaderProgressBar.hide()
                        }
                    }
                    is Response.Error -> {
                        //display error
                        Log.d(LOG_TAG, response.error)
                    }
                }
            }
        }
    }




    override fun onStart() {
        super.onStart()
        movieDetailsAdapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieDetailsAdapter.stopListening()
    }

    private fun getCurrentUser() = viewModel.getCurrentUser()

    private fun checkAndAddUserToDb() {
        dbRef.getReference("users").child(user.uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value === null) {
                    Log.d(LOG_TAG, "No user in db, need to add")
                    val userRef = dbRef.getReference("users").child(user.uid)
                    userRef.child("email").setValue(user.email)
                    Log.d(LOG_TAG, "User added")
                } else {
                    Log.d(LOG_TAG, "User already exists in db")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(LOG_TAG, error.message)
            }
        })
    }

    fun initNextButton() {
        nextButton.setOnClickListener {
            Log.d(LOG_TAG, "button clicked")
            getSelectedSeatsByCurrentUser()
        }
    }

    fun getSelectedSeatsByCurrentUser() {
        if (view != null) {
            movieId?.let { viewModel.getSelectedSeatsByUser(it, user.uid) }?.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        //show progressBar
                        Log.d(LOG_TAG, "getSeatDetails() request is loading")

                        loaderProgressBar.show()
                    }
                    is Response.Success -> {
                        if (response.data.isNotEmpty()) {
                            //hide progressBar
                            Log.d(LOG_TAG, response.data.toString())
                            //navigate to next fragment with data
                            goToChooseDateTimeFragment(response.data)

                            loaderProgressBar.hide()
                        }
                    }
                    is Response.Error -> {
                        //display error
                        Log.d(LOG_TAG, response.error)
                    }
                }
            }
        }
    }

    fun goToChooseDateTimeFragment(listOfSelectedSeats: List<String>) {
        val listOfSelectedSeatsBundle = Bundle()
        listOfSelectedSeatsBundle.putStringArrayList("listOfSelectedSeats", ArrayList(listOfSelectedSeats))
        listOfSelectedSeatsBundle.putString("movieTitle", movie.title)
        findNavController().navigate(R.id.chooseDateTimeFragment, listOfSelectedSeatsBundle)
    }

    private fun initProgressBar(view: View) {
        loaderProgressBar = view.findViewById(R.id.loadingProgressBar)
    }
}