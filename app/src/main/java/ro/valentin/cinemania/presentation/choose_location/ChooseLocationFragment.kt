package ro.valentin.cinemania.presentation.choose_location

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R
import ro.valentin.cinemania.presentation.movie_details.MovieDetailsActivity


class ChooseLocationFragment : Fragment(R.layout.fragment_choose_location) {
    private lateinit var selectLocationAdapter: SelectLocationAdapter
    private lateinit var selectLocationList: List<String>
    private lateinit var selectLocationRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSelectLocationRecyclerView(view)
    }

    private fun initSelectLocationRecyclerView(view: View) {
        selectLocationList = listOf(
            "Baneasa",
            "Mega Mall",
            "ParkLake",
            "Promenada"

        )
        selectLocationRecyclerView = view.findViewById(R.id.locationRecyclerView)

        selectLocationAdapter = SelectLocationAdapter(selectLocationList, object: SelectLocationAdapter.OnSelectLocationClickListener {
            override fun onSelectLocationClickListener(itemView: View) {
                val locationTextView = itemView.findViewById<TextView>(R.id.locationTextView)

                goToChooseDateTimeFragment(locationTextView.text.toString())
            }
        })

        selectLocationRecyclerView.adapter = selectLocationAdapter
    }

    fun goToChooseDateTimeFragment(location: String) {
        val locationBundle = Bundle()
        locationBundle.putSerializable("selectedLocation", location)

        findNavController().navigate(R.id.chooseDateTimeFragment, locationBundle)
    }
}