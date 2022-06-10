package ro.valentin.cinemania.presentation.movie_details

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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
    private lateinit var firebaseMovieRef: DatabaseReference
    private lateinit var dataBinding: MovieDetailsDataBinding
    private lateinit var movieDetailsAdapter: MovieDetailsAdapter
    private lateinit var seats: MutableList<Seat>
    private var movieId: Int? = 0
    private lateinit var user: FirebaseUser
    private lateinit var nextButton: Button
    private lateinit var loaderProgressBar: ProgressBar
    private lateinit var movie: Movie
    private lateinit var selectedInformation: HashMap<String, String>
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var selectedLocation: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = MovieDetailsDataBinding.inflate(inflater, container, false)

        dataBinding.descriptionTextView.movementMethod = ScrollingMovementMethod()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seats = getSeats()
        movieId = getMovieIdFromIntent()
         user = getCurrentUser()!!
        nextButton = view.findViewById(R.id.nextButton)
        getInformationFromBundle()



        firebaseMovieRef = dbRef.getReference("movies").child(movieId.toString()).child(selectedLocation).child(selectedDate).child(selectedTime).child("seats")
        initProgressBar(view)

        //alegere cinema
        //ecran alegere ora/data inainte de a alege locurile -> DONE
        //serviciu de simulare plata -> https://github.com/gjyoung1974/vgs-cardform-demo

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
        val query: Query = firebaseMovieRef
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
                        Log.d(LOG_TAG, "movie data is loading")
                    }
                    is Response.Success -> {
                        movie = response.data.toMovie()
                        dataBinding.setVariable(BR.movie, movie)
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
            firebaseMovieRef.child(seat.number.toString()).addListenerForSingleValueEvent(object: ValueEventListener {
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
        firebaseMovieRef.child(seat.number!!)
            .updateChildren(map)
    }

    private fun fetchSeatsDetails() {
        if (view != null) {
            movieId?.let { viewModel.getSeats(it, selectedLocation, selectedDate, selectedTime) }?.observe(viewLifecycleOwner) { response ->
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
                            loaderProgressBar.hide()
                        } else {
                            Log.d(LOG_TAG, "data is not there, need to push it")
                            firebaseMovieRef.setValue(seats)
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

    private fun getSelectedSeatsByCurrentUser() {
        if (view != null) {
            movieId?.let { viewModel.getSelectedSeatsByUser(it, selectedLocation, selectedDate, selectedTime, user.uid) }?.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        //show progressBar
                        Log.d(LOG_TAG, "getSelectedSeatsByCurrentUser() request is loading")

                    }
                    is Response.Success -> {
                        if (response.data.isNotEmpty()) {
                            //hide progressBar
                            Log.d(LOG_TAG, response.data.toString())
                            //navigate to next fragment with data
                            goToFinishFragment(response.data)
                        } else {
                            Toast.makeText(context, "You must selected at least one seat", Toast.LENGTH_SHORT).show()
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

    private fun goToFinishFragment(listOfSelectedSeats: List<String>) {
        val listOfSelectedInformationBundle = Bundle()
        listOfSelectedInformationBundle.putStringArrayList("listOfSelectedSeats", ArrayList(listOfSelectedSeats))
        listOfSelectedInformationBundle.putSerializable("selectedInformation", selectedInformation)
        listOfSelectedInformationBundle.putString("movieTitle", movie.title)
        listOfSelectedInformationBundle.putString("selectedLocation", selectedLocation)

        Log.d(LOG_TAG, "Go to FinishFragment")

        findNavController().navigate(R.id.orderDetailsFragment, listOfSelectedInformationBundle)
    }

    private fun initProgressBar(view: View) {
        loaderProgressBar = view.findViewById(R.id.loadingProgressBar)
    }

    private fun getInformationFromBundle() = arguments?.let {
            selectedInformation = it.getSerializable("selectedInformation") as HashMap<String, String>
            selectedDate = selectedInformation["date"].toString()
            selectedTime = selectedInformation["hour"].toString()
            selectedLocation = it.getString("selectedLocation").toString()
        }

}