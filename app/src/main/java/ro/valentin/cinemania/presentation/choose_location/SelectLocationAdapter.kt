package ro.valentin.cinemania.presentation.choose_location

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants
import ro.valentin.cinemania.domain.model.AvailableHour

class SelectLocationAdapter(
    private val dataSet: List<String>,
    private val onSelectLocationClickListener: OnSelectLocationClickListener
) : RecyclerView.Adapter<SelectLocationAdapter.SelectLocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectLocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_select, parent, false)

        return SelectLocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectLocationViewHolder, position: Int) {
        val item = dataSet[position]

        holder.bind(item)
    }

    override fun getItemCount() = dataSet.size

    inner class SelectLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(item: String) {
            itemView.findViewById<TextView>(R.id.locationTextView).text = item
            itemView.setOnClickListener { onSelectLocationClickListener.onSelectLocationClickListener(itemView)
            }

        }
    }

    interface OnSelectLocationClickListener {
        fun onSelectLocationClickListener(itemView: View)
    }



}