package cz.mamiyaza.mvp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import cz.mamiyaza.common.adapters.MainAdapter
import cz.mamiyaza.common.databinding.IncludeMainScreenBinding
import cz.mamiyaza.common.model.ApiMovieLite
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * TODO add class description
 */
@AndroidEntryPoint
class MainFragment : Fragment(), MainPresenter.MainView {

    private var _binding: IncludeMainScreenBinding? = null

    private val binding get() = _binding!!
    @Inject lateinit var presenter: MainPresenter
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeMainScreenBinding.inflate(inflater, container, false)
        mainAdapter = MainAdapter(object : MainAdapter.ItemClickListener {
            override fun onPostClicked(movie: ApiMovieLite) { }
        })

        binding.recyclerView.adapter = mainAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        return binding.root
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
        mainAdapter.statisticList(movies)
    }

    override fun clearMovies() {
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