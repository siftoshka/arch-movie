package cz.mamiyaza.amovie.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cz.mamiyaza.common.R
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite

/**
 * Main Fragment of the MVVM Project.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: IncludeMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeMainScreenBinding.inflate(inflater, container, false)
        mainAdapter = MainAdapter(object : MainAdapter.ItemClickListener {
            override fun onPostClicked(movie: ApiMovieLite) { }
        })
        binding.recyclerView.adapter = mainAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        viewModel.getMovies()
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
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
            mainAdapter.statisticList(data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_search -> {
                val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
                requireView().findNavController().navigate(action)
                true
            }
            R.id.main_saved -> {
                print("SAVED")
                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }
}