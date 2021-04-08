package cz.mamiyaza.mvi.saved

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cz.mamiyaza.common.adapters.SavedMoviesAdapter
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.databinding.IncludeSavedScreenBinding
import cz.mamiyaza.common.utils.Constants.MOVIE_ID
import cz.mamiyaza.mvi.R

/**
 * Saved Movies Fragment of the MVVM Project.
 */
@AndroidEntryPoint
class SavedMoviesFragment : Fragment(), SavedMoviesAdapter.ItemClickListener {

    private var _binding: IncludeSavedScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var savedMoviesAdapter: SavedMoviesAdapter

    private val viewModel: SavedMoviesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeSavedScreenBinding.inflate(inflater, container, false)
        savedMoviesAdapter = SavedMoviesAdapter(this)
        binding.recyclerView.adapter = savedMoviesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.toolbar.inflateMenu(R.menu.menu_saved)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.savedMovies.observe(viewLifecycleOwner) { data ->
            savedMoviesAdapter.statisticList(data)
        }
    }

    override fun onPostClicked(movie: Movie) {
        val args = Bundle().apply {
            putInt(MOVIE_ID, movie.id)
        }
        findNavController().navigate(R.id.action_savedFragment_to_movieFragment, args)
    }

    override fun onPostLongClicked(movie: Movie) {
        openDialog(movie)
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

    private fun openDialog(movie: Movie) {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Delete Movie")
        builder.setMessage("Are you sure to delete saved movie?")
        builder.setPositiveButton(android.R.string.yes) { _, _ -> viewModel.deleteMovie(movie) }
        builder.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun openDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Delete All Movies")
        builder.setMessage("Are you sure to delete all saved movies?")
        builder.setPositiveButton(android.R.string.yes) { _, _ -> viewModel.deleteMovies() }
        builder.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}