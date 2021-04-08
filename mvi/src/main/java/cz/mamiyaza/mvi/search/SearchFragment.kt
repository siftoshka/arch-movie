package cz.mamiyaza.mvi.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeSearchScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.MOVIE_ID
import cz.mamiyaza.common.utils.DataState
import cz.mamiyaza.mvi.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Search Fragment of MVI Project.
 */
@AndroidEntryPoint
class SearchFragment : Fragment(), MainAdapter.ItemClickListener {

    private var _binding: IncludeSearchScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeSearchScreenBinding.inflate(inflater, container, false)
        mainAdapter = MainAdapter(this)
        binding.recyclerViewSearch.adapter = mainAdapter
        binding.recyclerViewSearch.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchBar.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.query = newText
                viewModel.setStateEvent(SavedStateEvent.GetSearchEvent)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<List<ApiMovieLite>> -> {
                    mainAdapter.addAllMedia(dataState.data)
                }
            }
        })
    }

    override fun onPostClicked(movie: ApiMovieLite) {
        val args = Bundle().apply { putInt(MOVIE_ID, movie.movieId) }
        findNavController().navigate(R.id.action_searchFragment_to_movieFragment, args)
    }
}