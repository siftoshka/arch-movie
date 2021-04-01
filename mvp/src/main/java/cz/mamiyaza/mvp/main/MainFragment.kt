package cz.mamiyaza.mvp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants
import cz.mamiyaza.mvp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main Fragment of the MVP Project.
 */
@AndroidEntryPoint
class MainFragment : Fragment(), MainPresenter.MainView, MainAdapter.ItemClickListener {

    private var _binding: IncludeMainScreenBinding? = null

    private val binding get() = _binding!!
    @Inject lateinit var presenter: MainPresenter
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeMainScreenBinding.inflate(inflater, container, false)
        presenter.attachView(this)
        mainAdapter = MainAdapter(this)
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        binding.recyclerView.adapter = mainAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var page = 2
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                try {
                    if (!binding.recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        presenter.addMoreMovies(page)
                        page++
                    }
                } catch (ignored: Exception) { }
            }
        })
    }

    override fun onPostClicked(movie: ApiMovieLite) {
        val args = Bundle().apply {
            putInt(Constants.MOVIE_ID, movie.movieId)
        }
        findNavController().navigate(R.id.action_mainFragment_to_movieFragment, args)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_search -> {
                val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
                findNavController().navigate(action)
                true
            }
            R.id.main_saved -> {
                val action = MainFragmentDirections.actionMainFragmentToSavedFragment()
                findNavController().navigate(action)
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

    override fun showMovieList(movies: List<ApiMovieLite>) {
        mainAdapter.addAllMedia(movies)
    }

    override fun showMoreMovieList(movies: List<ApiMovieLite>) {
        mainAdapter.showMoreMedia(movies)
    }

    override fun showLoading() {
        binding.loadingScreen.root.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingScreen.root.visibility = View.GONE
    }

    override fun showConnectionError() {
        binding.errorScreen.root.visibility = View.VISIBLE
    }
}