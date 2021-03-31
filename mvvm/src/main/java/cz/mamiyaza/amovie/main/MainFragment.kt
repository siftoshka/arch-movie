package cz.mamiyaza.amovie.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.mamiyaza.amovie.R
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.utils.Constants.MOVIE_ID
import dagger.hilt.android.AndroidEntryPoint


/**
 * Main Fragment of the MVVM Project.
 */
@AndroidEntryPoint
class MainFragment : Fragment(), MainAdapter.ItemClickListener {

    private var _binding: IncludeMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IncludeMainScreenBinding.inflate(inflater, container, false)
        mainAdapter = MainAdapter(this)
        binding.recyclerView.adapter = mainAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        viewModel.getMovies()
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        binding.errorScreen.refreshButton.setOnClickListener {
            viewModel.getMovies()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) binding.loadingScreen.root.visibility = View.VISIBLE
            else binding.loadingScreen.root.visibility = View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error) binding.errorScreen.root.visibility = View.VISIBLE
            else binding.errorScreen.root.visibility = View.GONE
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            mainAdapter.addAllMedia(data)
        }

        viewModel.moreData.observe(viewLifecycleOwner) { data ->
            mainAdapter.showMoreMedia(data)
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var page = 2
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                try {
                    if (!binding.recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        viewModel.addMoreMovies(page)
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