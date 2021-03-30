package cz.mamiyaza.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.mamiyaza.common.databinding.IncludeMovieBinding
import cz.mamiyaza.common.model.ApiMovieLite

/**
 * Adapter for list of trending movies.
 */
class MainAdapter(private val clickListener: ItemClickListener) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    interface ItemClickListener {
        fun onPostClicked(movie: ApiMovieLite)
    }

    inner class MainViewHolder(val binding: IncludeMovieBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ApiMovieLite>() {
        override fun areItemsTheSame(oldItem: ApiMovieLite, newItem: ApiMovieLite): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: ApiMovieLite, newItem: ApiMovieLite): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val diff = AsyncListDiffer(this, diffCallback)

    fun statisticList(list: List<ApiMovieLite>) = diff.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = IncludeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder) {
            with(diff.currentList[position]) {
                binding.posterImage.load("https://image.tmdb.org/t/p/original$movieImage")
                binding.posterTitle.text = movieTitle
                mClickListener = clickListener
                itemView.apply {
                    mClickListener?.onPostClicked(movie = this@with)
                }
            }
        }
        val movie = diff.currentList[position]
        holder.itemView.apply {
            mClickListener = clickListener
            holder.itemView.setOnLongClickListener {
                mClickListener?.onPostClicked(movie)
                true
            }
        }
    }

}