package cz.mamiyaza.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.databinding.IncludeGenresBinding
import cz.mamiyaza.common.databinding.IncludeMovieBinding
import cz.mamiyaza.common.databinding.IncludeSavedMoviesBinding
import cz.mamiyaza.common.model.ApiMovieGenre
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.IMAGE_URL

/**
 * Adapter for saved movies screen.
 */
class SavedMoviesAdapter(private val clickListener: ItemClickListener) : RecyclerView.Adapter<SavedMoviesAdapter.MainViewHolder>() {

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    interface ItemClickListener {
        fun onPostClicked(movie: Movie)
        fun onPostLongClicked(movie: Movie)
    }

    inner class MainViewHolder(val binding: IncludeSavedMoviesBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val diff = AsyncListDiffer(this, diffCallback)

    fun statisticList(list: List<Movie>) = diff.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = IncludeSavedMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val movie = diff.currentList[position]
        with(holder) {
            with(diff.currentList[position]) {
                binding.movieText.text = title
                mClickListener = clickListener
                holder.itemView.setOnClickListener {
                    mClickListener?.onPostClicked(movie = movie)
                }
                holder.itemView.setOnLongClickListener {
                    mClickListener?.onPostLongClicked(movie = movie)
                    true
                }
            }
        }
    }
}