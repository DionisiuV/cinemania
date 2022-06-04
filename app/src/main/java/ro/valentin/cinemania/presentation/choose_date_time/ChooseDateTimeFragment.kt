package ro.valentin.cinemania.presentation.choose_date_time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.domain.model.AvailableHour
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.HashMap

class ChooseDateTimeFragment : Fragment(R.layout.fragment_choose_date_time) {
    var listOfSelectedSeats: List<String>? = null
    private lateinit var nextButton: Button
    private lateinit var selectTimeAdapter: SelectTimeAdapter
    private lateinit var selectTimeList: List<AvailableHour>
    private lateinit var selectTimeRecyclerView: RecyclerView
    private lateinit var selectedInformation: HashMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedInformation = hashMapOf()
        listOfSelectedSeats = getListOfSelectedSeatsFromBundle()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initSelectTimeRecyclerView(view)
        initDatePicker(view)
        initNextButton(view)
    }

    fun initSelectTimeRecyclerView(view: View) {
        selectTimeList = listOf(
            AvailableHour("09:30"),
            AvailableHour("13:50"),
            AvailableHour("18:00"),
        )
        selectTimeRecyclerView = view.findViewById(R.id.timeRecyclerView)
        var lastSelected = ""
        selectTimeAdapter = SelectTimeAdapter(selectTimeList, object: SelectTimeAdapter.OnSelectTimeClickListener {
            override fun onSelectTimeClickListener(item: AvailableHour, position: Int) {
                var alreadySelected = false


                selectTimeList.forEach { hour ->
                    if(!hour.available && lastSelected != item.time) {
                        alreadySelected = true
                    }
                }

                if(!alreadySelected) {
                    selectTimeList[position].available = !item.available
                    lastSelected = item.time
                    pickSelectedHour(item.time)
                    selectTimeAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "You can select only once a time", Toast.LENGTH_SHORT).show()
                }
            }

        })
        selectTimeRecyclerView.adapter = selectTimeAdapter
    }

    fun initDatePicker(view: View) {
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        datePicker.minDate = calendar.timeInMillis
        datePicker.maxDate = calendar.timeInMillis + (1000*60*60*24*5)

        var selectedDate = "${calendar.get(Calendar.YEAR)}-${Month.of(calendar.get(Calendar.MONTH)).getDisplayName(TextStyle.FULL, Locale.US)}-${calendar.get(Calendar.DAY_OF_MONTH)}"
        pickSelectedDate(selectedDate)
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
            DatePicker.OnDateChangedListener { _, year, month, day ->
                selectedDate = "$year-${Month.of(month+1).getDisplayName(TextStyle.FULL, Locale.US)}-$day"
                Log.d(LOG_TAG, selectedDate)
                pickSelectedDate(selectedDate)

            })
    }
    fun pickSelectedDate(date: String) {
        selectedInformation["date"] = date
    }
    fun pickSelectedHour(hour: String) {
        selectedInformation["hour"] = hour
    }

    fun getSelectedInformation() {
        if(selectedInformation.containsKey("date") && selectedInformation.containsKey("hour")) {
            Log.d(LOG_TAG, selectedInformation.toString())
            goToFinishFragment()
        } else {
            Log.d(LOG_TAG, "Not enough information")

        }

    }

    fun initNextButton(view: View) {
        nextButton = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            getSelectedInformation()
        }
    }

    fun getListOfSelectedSeatsFromBundle(): List<String>? = arguments?.getStringArrayList("listOfSelectedSeats")

    fun goToFinishFragment() {
            val listOfSelectedInformationBundle = Bundle()
            listOfSelectedInformationBundle.putStringArrayList("listOfSelectedSeats", ArrayList(listOfSelectedSeats as MutableList))
            listOfSelectedInformationBundle.putSerializable("selectedInformation", selectedInformation)

        Log.d(LOG_TAG, "Go to FinishFragment")
        Log.d(LOG_TAG, listOfSelectedSeats.toString())
            findNavController().navigate(R.id.orderDetailsFragment, listOfSelectedInformationBundle)
    }
}