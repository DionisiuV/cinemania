package ro.valentin.cinemania.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R

class SelectTimeAdapter(
   private val dataSet: List<String>
) : RecyclerView.Adapter<SelectTimeAdapter.SelectTimeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectTimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_select, parent, false)

        return SelectTimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectTimeViewHolder, position: Int) {
        val item = dataSet[position]

        holder.bind(item)
    }

    override fun getItemCount() = dataSet.size

    inner class SelectTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView)
        fun bind(text: String) {
            timeTextView.text = text
        }
    }
}