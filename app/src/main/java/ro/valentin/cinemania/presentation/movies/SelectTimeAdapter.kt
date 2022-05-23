package ro.valentin.cinemania.presentation.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.domain.model.Movie
import ro.valentin.cinemania.generated.callback.OnClickListener

class SelectTimeAdapter(
   private val dataSet: List<String>,
   private val listener: (String) -> Unit
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


        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        fun bind(text: String) {
            timeTextView.text = text
            timeTextView.setOnClickListener { listener(text) } //any downside on that? i should use classic interface approach?
        }
    }



}