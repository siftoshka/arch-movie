package cz.mamiyaza.mvp.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeSearchScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.MOVIE_ID
import cz.mamiyaza.mvp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Search Fragment of MVVM Project.
 */
@AndroidEntryPoint
class SearchFragment : Fragment(), SearchPresenter.SearchView, MainAdapter.ItemClickListener {

    private var _binding: IncludeSearchScreenBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var presenter: SearchPresenter
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeSearchScreenBinding.inflate(inflater, container, false)
        presenter.attachView(this)
        mainAdapter = MainAdapter(this)
        binding.recyclerViewSearch.adapter = mainAdapter
        binding.recyclerViewSearch.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchBar.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.searchMovies(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
        return binding.root
    }

    override fun onPostClicked(movie: ApiMovieLite) {
        val args = Bundle().apply { putInt(MOVIE_ID, movie.movieId) }
        findNavController().navigate(R.id.action_searchFragment_to_movieFragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showSearchList(movies: List<ApiMovieLite>) {
        mainAdapter.addAllMedia(movies)
    }
}