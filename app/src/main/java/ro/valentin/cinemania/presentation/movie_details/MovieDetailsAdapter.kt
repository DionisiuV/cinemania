package ro.valentin.cinemania.presentation.movie_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R

class MovieDetailsAdapter(private val seats: List<String>) : RecyclerView.Adapter<MovieDetailsAdapter.SeatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)

        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val item = seats[position]

        holder.bind(item)
    }

    override fun getItemCount() = seats.size

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            itemView.findViewById<TextView>(R.id.seatTextView).text = item
        }

    }
}