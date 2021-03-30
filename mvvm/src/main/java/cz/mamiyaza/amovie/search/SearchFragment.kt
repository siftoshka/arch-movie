package cz.mamiyaza.amovie.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cz.mamiyaza.amovie.main.MainViewModel
import cz.mamiyaza.common.R
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.databinding.IncludeSearchScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import dagger.hilt.android.AndroidEntryPoint

/**
 * TODO add class description
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: IncludeSearchScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeSearchScreenBinding.inflate(inflater, container, false)
        mainAdapter = MainAdapter(object : MainAdapter.ItemClickListener {
            override fun onPostClicked(movie: ApiMovieLite) { }
        })
        binding.recyclerViewSearch.adapter = mainAdapter
        binding.recyclerViewSearch.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchBar.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchMovies(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            mainAdapter.statisticList(data)
        }
    }

}