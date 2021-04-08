package cz.mamiyaza.mvi.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import cz.mamiyaza.common.adapters.GenresAdapter
import cz.mamiyaza.common.databinding.IncludeMovieScreenBinding
import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.utils.Constants.IMAGE_URL
import cz.mamiyaza.common.utils.Constants.MOVIE_ID
import cz.mamiyaza.common.utils.CurrencyFormatter
import cz.mamiyaza.common.utils.DataState
import cz.mamiyaza.mvi.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Movie Fragment of MVI Project.
 */
@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: IncludeMovieScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var genresAdapter: GenresAdapter

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = IncludeMovieScreenBinding.inflate(inflater, container, false)
        genresAdapter = GenresAdapter()
        binding.movieGenres.adapter = genresAdapter
        binding.movieGenres.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.errorScreen.refreshButton.setOnClickListener {
            viewModel.setStateEvent(MovieStateEvent.GetMovieEvent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.getInt(MOVIE_ID, -1) ?: -1
        viewModel.movieId = id
        viewModel.setStateEvent(MovieStateEvent.GetMovieEvent)

        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<ApiMovie> -> {
                    binding.loadingScreen.root.visibility = View.GONE
                    binding.mainScreen.visibility = View.VISIBLE
                    loadScreen(dataState.data)
                }
                is DataState.Error -> {
                    binding.loadingScreen.root.visibility = View.GONE
                    binding.mainScreen.visibility = View.GONE
                    binding.errorScreen.root.visibility = View.VISIBLE
                }
                is DataState.Loading -> {
                    binding.mainScreen.visibility = View.GONE
                    binding.loadingScreen.root.visibility = View.VISIBLE
                }
            }
        })

        binding.saveButton.tag = Integer.valueOf(R.drawable.ic_star)
        viewModel.savedMovies.observe(viewLifecycleOwner) { movies ->
            if (movies.find { it.id == viewModel.movieId } != null) {
                binding.saveButton.setImageResource(R.drawable.ic_star_filled)
                binding.saveButton.tag = Integer.valueOf(R.drawable.ic_star_filled)
            } else {
                binding.saveButton.setImageResource(R.drawable.ic_star)
                binding.saveButton.tag = Integer.valueOf(R.drawable.ic_star)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadScreen(movie: ApiMovie) {
        binding.posterBackground.load("$IMAGE_URL${movie.backdropPath}")
        binding.posterMoviePost.load("$IMAGE_URL${movie.posterPath}")
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
            if (binding.saveButton.tag as Int == R.drawable.ic_star) viewModel.saveMovie(movie.title)
            else viewModel.deleteMovie(movie.title)
        }
    }
}