package ro.valentin.cinemania.presentation.choose_date_time

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.domain.model.AvailableHour


class SelectTimeAdapter(
   private val dataSet: List<AvailableHour>,
   private val onSelectTimeClickListener: OnSelectTimeClickListener
) : RecyclerView.Adapter<SelectTimeAdapter.SelectTimeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectTimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_select, parent, false)

        return SelectTimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectTimeViewHolder, position: Int) {
        val item = dataSet[position]

        holder.bind(item, position)
    }

    override fun getItemCount() = dataSet.size

    inner class SelectTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        fun bind(item: AvailableHour, position: Int) {
            timeTextView.text = item.time
            timeTextView.setOnClickListener { onSelectTimeClickListener.onSelectTimeClickListener(item, position)
                if(item.available) {
                    Log.d(LOG_TAG, "Item available, change color")
                    itemView.setBackgroundResource(R.color.grey)
                    Log.d(LOG_TAG, dataSet.toString())
                } else {
                    Log.d(LOG_TAG, "Item not available, change color")
                    Log.d(LOG_TAG, dataSet.toString())
                    itemView.setBackgroundResource(R.color.red)
                }
            }

        }
    }

    interface OnSelectTimeClickListener {
        fun onSelectTimeClickListener(item: AvailableHour, position: Int)
    }



}