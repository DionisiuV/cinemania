package ro.valentin.cinemania.presentation.movie_details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ro.valentin.cinemania.R
import ro.valentin.cinemania.domain.model.Seat

class MovieDetailsAdapter(
    private val onSeatClick: OnSeatClickListener,
    options: FirebaseRecyclerOptions<Seat>
    ) : FirebaseRecyclerAdapter<Seat, MovieDetailsAdapter.SeatViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int, seat: Seat) {
        holder.bind(seat)
        holder.itemView.setOnClickListener {
            onSeatClick.onSeatClick(seat, position)
        }
    }

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Seat) {
            itemView.findViewById<TextView>(R.id.seatTextView).text = item.number
            if(item.available) {
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