package ro.valentin.cinemania.presentation.movie_details

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.R
import ro.valentin.cinemania.domain.model.Movie
import ro.valentin.cinemania.domain.model.Seat

class MovieDetailsAdapter(
    private var seats: List<Seat>,
    private val onSeatClick: OnSeatClickListener
    ) : RecyclerView.Adapter<MovieDetailsAdapter.SeatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)

        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val item = seats[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            onSeatClick.onSeatClick(item, position)
        }
    }

    override fun getItemCount() = seats.size

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Seat) {
            itemView.findViewById<TextView>(R.id.seatTextView).text = item.number
            if(item.available == true ) {
                itemView.setBackgroundColor(Color.parseColor("#00FF00"))
            } else {
                itemView.setBackgroundColor(Color.parseColor("#FF0000"))
            }
        }
    }

    interface OnSeatClickListener {
        fun onSeatClick(seat: Seat, position: Int)
    }
}