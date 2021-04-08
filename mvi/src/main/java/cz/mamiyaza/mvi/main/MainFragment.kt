package cz.mamiyaza.mvi.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.MOVIE_ID
import cz.mamiyaza.common.utils.DataState
import cz.mamiyaza.mvi.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Fragment of the MVI Project.
 */
@AndroidEntryPoint
class MainFragment : Fragment(), MainAdapter.ItemClickListener {

    private var _binding: IncludeMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeMainScreenBinding.inflate(inflater, container, false)
        mainAdapter = MainAdapter(this)
        binding.recyclerView.adapter = mainAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        viewModel.setStateEvent(MainStateEvent.GetMoviesEvent)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<List<ApiMovieLite>> -> {
                    binding.loadingScreen.root.visibility = View.GONE
                    mainAdapter.addAllMedia(dataState.data)
                }
                is DataState.Error -> {
                    binding.loadingScreen.root.visibility = View.GONE
                    binding.errorScreen.root.visibility = View.VISIBLE
                }
                is DataState.Loading -> {
                    binding.loadingScreen.root.visibility = View.VISIBLE
                }
            }
        })
        viewModel.moreDataState.observe(viewLifecycleOwner, { moreDataState ->
            when (moreDataState) {
                is DataState.Success<List<ApiMovieLite>> -> {
                    mainAdapter.showMoreMedia(moreDataState.data)
                }
            }
        })

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var page = 2
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                try {
                    if (!binding.recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        viewModel.page = page
                        viewModel.setStateEvent(MainStateEvent.GetMoreMovies)
                        page++
                    }
                } catch (ignored: Exception) { }
            }
        })
    }

    override fun onPostClicked(movie: ApiMovieLite) {
        val args = Bundle().apply {
            putInt(MOVIE_ID, movie.movieId)
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
}