package ro.valentin.cinemania.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ro.valentin.cinemania.databinding.MovieDataBinding
import ro.valentin.cinemania.domain.model.Movie
import ro.valentin.cinemania.BR
import ro.valentin.cinemania.R
import kotlin.math.min

class MoviesAdapter(
    private val movieList: List<Movie>,
    private val onMovieClickListener: OnMovieClickListener
    ) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    private val limit = 5
    private lateinit var selectTimeAdapter: SelectTimeAdapter
    private lateinit var selectTimeList: List<String>
    private lateinit var selectTimeRecyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = MovieDataBinding.inflate(layoutInflater, parent, false)



        //for testing
        //idk where to initiate and set second recyclerview
        selectTimeList = listOf(
            "09:30",
            "13:50",
            "18:90"
        )
        selectTimeAdapter = SelectTimeAdapter(selectTimeList)
        selectTimeRecyclerView = dataBinding.timeRecyclerView
        selectTimeRecyclerView.adapter = selectTimeAdapter

        return MovieViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movieList[position]
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item)
    }

    override fun getItemCount() = movieList.size

    inner class MovieViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(movie: Movie?) {
            dataBinding.setVariable(BR.movie, movie)
            dataBinding.setVariable(BR.onMovieClickListener, onMovieClickListener)
        }

    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }
}