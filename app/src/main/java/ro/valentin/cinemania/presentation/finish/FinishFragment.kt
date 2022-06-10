package ro.valentin.cinemania.presentation.finish

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.presentation.main.MainActivity
import ro.valentin.cinemania.presentation.movie_details.MovieDetailsActivity
import ro.valentin.cinemania.presentation.movie_details.MovieDetailsViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@AndroidEntryPoint
class FinishFragment : Fragment(R.layout.fragment_finish) {
    var selectedSeats: List<String>? = null
    private val viewModel by viewModels<MovieDetailsViewModel>()
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var selectedLocation: String
    private lateinit var movieTitle: String
    private lateinit var user: FirebaseUser
    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = getCurrentUser()!!

        getInformationFromBundle()
        initFinishButton(view)
        setTextView(view)
    }

    private fun getInformationFromBundle() {
        arguments?.let {
            val selectedInfo = it.getSerializable("selectedInformation") as HashMap<String, String>
            selectedSeats = it.getStringArrayList("listOfSelectedSeats")
            movieTitle = it.getString("movieTitle").toString()
            selectedDate = selectedInfo["date"].toString()
            selectedTime = selectedInfo["hour"].toString()
            selectedLocation = it.getString("selectedLocation").toString()
        }
    }

    private fun initFinishButton(view: View) {
        val finishButton = view.findViewById<Button>(R.id.finishButton)

        finishButton.setOnClickListener {
            addRecordToFirestore()
            goToMovieDetails()
        }
    }

    private fun setTextView(view: View) {
        view.findViewById<TextView>(R.id.locationTextView).text = "Selected Location: $selectedLocation"
        view.findViewById<TextView>(R.id.dateAndTimeTextView).text = "Selected Date: $selectedDate\nSelected Time: $selectedTime"
        view.findViewById<TextView>(R.id.movieTitleTextView).text = "Movie title: $movieTitle"
        view.findViewById<TextView>(R.id.seatsTextView).text = "Selected seats: ${selectedSeats?.joinToString(", ")}"
    }

    private fun getCurrentUser() = viewModel.getCurrentUser()

    private fun addRecordToFirestore() {
        val mail: MutableMap<String, Any> = HashMap()
        mail["to"] = Arrays.asList(user.email.toString())
        val message: MutableMap<String, Any> = HashMap()
        message["subject"] = "Cinemania - Order Details"
        message["html"] = "Thanks for choosing Cinema ${selectedLocation}<br>" +
                "We are waiting for you at the movie ${movieTitle} on ${selectedDate} at ${selectedTime}<br> " +
                "Your selected seats: ${selectedSeats?.joinToString(", ")}<br><br><br>" +
                "*You will pay 7$ for each selected seat."
        mail["message"] = message

        firebaseFirestore.collection("mail")
            .add(mail)
            .addOnSuccessListener { documentReference ->
                Log.d(LOG_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(LOG_TAG, "Error adding document", e)
            }
    }

    private fun goToMovieDetails() {
        val movieDetailsIntent = Intent(context, MainActivity::class.java)

        Toast.makeText(context, "Success - Your order has been placed!", Toast.LENGTH_LONG).show()

        startActivity(movieDetailsIntent)
    }
}