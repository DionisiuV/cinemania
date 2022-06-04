package ro.valentin.cinemania.presentation.finish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.presentation.movie_details.MovieDetailsViewModel

@AndroidEntryPoint
class FinishFragment : Fragment(R.layout.fragment_finish) {
    var selectedSeats: List<String>? = null
    private val viewModel by viewModels<MovieDetailsViewModel>()
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var user: FirebaseUser

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


        }
    }

    fun setTextView(view: View) {
        view.findViewById<TextView>(R.id.dateAndTimeTextView).text = "Selected Date: $selectedDate\nSelected Time: $selectedTime"
        view.findViewById<TextView>(R.id.seatsTextView).text = "Selected seats: ${selectedSeats?.joinToString()}"
    }

    private fun getCurrentUser() = viewModel.getCurrentUser()
}