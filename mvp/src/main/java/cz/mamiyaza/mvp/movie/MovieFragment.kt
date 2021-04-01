package cz.mamiyaza.mvp.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import cz.mamiyaza.common.adapters.GenresAdapter
import cz.mamiyaza.common.databinding.IncludeMovieScreenBinding
import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.utils.Constants
import cz.mamiyaza.common.utils.CurrencyFormatter
import cz.mamiyaza.mvp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Movie Fragment of the MVP Project.
 */
@AndroidEntryPoint
class MovieFragment : Fragment(), MoviePresenter.MovieView {

    private var _binding: IncludeMovieScreenBinding? = null

    private val binding get() = _binding!!
    @Inject lateinit var presenter: MoviePresenter
    private lateinit var genresAdapter: GenresAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeMovieScreenBinding.inflate(inflater, container, false)
        presenter.attachView(this)
        genresAdapter = GenresAdapter()
        binding.movieGenres.adapter = genresAdapter
        binding.movieGenres.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.errorScreen.refreshButton.setOnClickListener {
            presenter.loadMovie()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.getInt(Constants.MOVIE_ID, -1) ?: -1
        presenter.loadMovie(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun checkMovie(isSaved: Boolean) {
        if (isSaved) {
            binding.saveButton.setImageResource(R.drawable.ic_star_filled)
            binding.saveButton.tag = Integer.valueOf(R.drawable.ic_star_filled)
        } else {
            binding.saveButton.setImageResource(R.drawable.ic_star)
            binding.saveButton.tag = Integer.valueOf(R.drawable.ic_star)
        }
    }

    override fun showMovie(movie: ApiMovie) {
        binding.mainScreen.visibility = View.VISIBLE
        binding.posterBackground.load("${Constants.IMAGE_URL}${movie.backdropPath}")
        binding.posterMoviePost.load("${Constants.IMAGE_URL}${movie.posterPath}")
        binding.posterTitle.text = movie.title
        binding.posterDate.text = movie.releaseDate
        binding.posterRate.text = movie.voteAverage.toString()
        binding.posterViews.text = "(${movie.voteCount})"
        binding.posterDuration.text = "${movie.runtime} ${resources.getString(R.string.minutes)}"
        binding.posterBudget.text = ": $ ${CurrencyFormatter.format(movie.budget)}"
        binding.posterRevenue.text = ": $ ${CurrencyFormatter.format(movie.revenue)}"
        genresAdapter.statisticList(movie.movieGenres)
        binding.posterDesc.text = movie.overview

        binding.saveButton.setOnClickListener {
            if (binding.saveButton.tag as Int == R.drawable.ic_star) presenter.saveMovie(movie.title)
            else presenter.deleteMovie(movie.title)
        }
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