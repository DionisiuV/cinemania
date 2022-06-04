package ro.valentin.cinemania.presentation.finish

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
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

    fun getInformationFromBundle() {
        arguments?.let {
            val selectedInfo = it.getSerializable("selectedInformation") as HashMap<String, String>
            selectedSeats = it.getStringArrayList("listOfSelectedSeats")
            selectedDate = selectedInfo["date"].toString()
            selectedTime = selectedInfo["hour"].toString()
        }
    }

    fun initFinishButton(view: View) {
        val finishButton = view.findViewById<Button>(R.id.finishButton)

        finishButton.setOnClickListener {
            Log.d(LOG_TAG, selectedSeats.toString())
            Log.d(LOG_TAG, selectedDate.toString())
            Log.d(LOG_TAG, selectedTime.toString())
            Log.d(LOG_TAG, user.email.toString())
            addRecordToFirestore()

        }
    }

    fun setTextView(view: View) {
        view.findViewById<TextView>(R.id.dateAndTimeTextView).text = "Selected Date: $selectedDate\nSelected Time: $selectedTime"
        view.findViewById<TextView>(R.id.seatsTextView).text = "Selected seats: ${selectedSeats?.joinToString()}"
    }

    private fun getCurrentUser() = viewModel.getCurrentUser()

    fun addRecordToFirestore() {
        val mail: MutableMap<String, Any> = HashMap()
        mail["to"] = Arrays.asList(user.email.toString())
        val message: MutableMap<String, Any> = HashMap()
        message["subject"] = "Cinemania - Order Details"
        message["html"] = "Selected date: ${selectedDate}<br> Selected time: ${selectedTime}<br> Selected seats: ${selectedSeats}"
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
}