package cz.mamiyaza.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.mamiyaza.common.databinding.IncludeMovieBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.IMAGE_URL

/**
 * Adapter for list of trending movies.
 */
class MainAdapter(private val clickListener: ItemClickListener) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var movies = arrayListOf<ApiMovieLite>()

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    interface ItemClickListener {
        fun onPostClicked(movie: ApiMovieLite)
    }

    inner class MainViewHolder(val binding: IncludeMovieBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = IncludeMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    fun addAllMedia(movies: List<ApiMovieLite>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun showMoreMedia(movies: List<ApiMovieLite>) {
        if (!this.movies.containsAll(movies)) {
            val position = this.movies.size
            this.movies.addAll(movies)
            notifyItemRangeInserted(position, movies.size)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder) {
            val movie = movies[position]
            binding.posterImage.load("$IMAGE_URL${movie.movieImage}")
            binding.posterTitle.text = movie.movieTitle
            mClickListener = clickListener
            holder.itemView.setOnClickListener {
                mClickListener?.onPostClicked(movie = movie)
            }
        }
    }
}