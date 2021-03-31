package cz.mamiyaza.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.mamiyaza.common.databinding.IncludeGenresBinding
import cz.mamiyaza.common.databinding.IncludeMovieBinding
import cz.mamiyaza.common.model.ApiMovieGenre
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.IMAGE_URL

/**
 * Adapter for list of trending movies.
 */
class GenresAdapter : RecyclerView.Adapter<GenresAdapter.MainViewHolder>() {

    inner class MainViewHolder(val binding: IncludeGenresBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ApiMovieGenre>() {
        override fun areItemsTheSame(oldItem: ApiMovieGenre, newItem: ApiMovieGenre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiMovieGenre, newItem: ApiMovieGenre): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val diff = AsyncListDiffer(this, diffCallback)

    fun statisticList(list: List<ApiMovieGenre>) = diff.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = IncludeGenresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        with(holder) {
            with(diff.currentList[position]) {
                binding.genreText.text = name
            }
        }
    }
}