package cz.mamiyaza.amovie.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeSearchScreenBinding.inflate(inflater, container, false)

        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

}