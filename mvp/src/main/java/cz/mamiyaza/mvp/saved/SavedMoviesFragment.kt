package cz.mamiyaza.mvp.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.adapters.SavedMoviesAdapter
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.databinding.IncludeSavedMoviesBinding
import cz.mamiyaza.common.databinding.IncludeSavedScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants
import cz.mamiyaza.mvp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Saved Movies Fragment of the MVP Project.
 */
@AndroidEntryPoint
class SavedMoviesFragment : Fragment(), SavedMoviesPresenter.SavedMoviesView, SavedMoviesAdapter.ItemClickListener {

    private var _binding: IncludeSavedScreenBinding? = null

    private val binding get() = _binding!!
    @Inject lateinit var presenter: SavedMoviesPresenter
    private lateinit var savedMoviesAdapter: SavedMoviesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeSavedScreenBinding.inflate(inflater, container, false)
        presenter.attachView(this)
        savedMoviesAdapter = SavedMoviesAdapter(this)
        binding.toolbar.inflateMenu(R.menu.menu_saved)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.recyclerView.adapter = savedMoviesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_delete -> {
                openDialog()
                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showMovies(movies: List<Movie>) {
        savedMoviesAdapter.statisticList(movies)
    }

    override fun updateWholeView() {
        savedMoviesAdapter.statisticList(emptyList())
    }

    override fun updateView(movies: List<Movie>) {
        savedMoviesAdapter.statisticList(movies)
    }

    override fun onPostClicked(movie: Movie) {
        val args = Bundle().apply {
            putInt(Constants.MOVIE_ID, movie.id)
        }
        findNavController().navigate(R.id.action_savedFragment_to_movieFragment, args)
    }

    override fun onPostLongClicked(movie: Movie) {
        openDialog(movie)
    }

    private fun openDialog(movie: Movie) {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Delete Movie")
        builder.setMessage("Are you sure to delete saved movie?")
        builder.setPositiveButton(android.R.string.yes) { _, _ -> presenter.deleteMovie(movie) }
        builder.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun openDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Delete All Movies")
        builder.setMessage("Are you sure to delete all saved movies?")
        builder.setPositiveButton(android.R.string.yes) { _, _ -> presenter.deleteMovies() }
        builder.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}