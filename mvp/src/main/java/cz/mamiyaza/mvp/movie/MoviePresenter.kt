package cz.mamiyaza.mvp.movie

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.repository.MainRepository
import cz.mamiyaza.common.repository.ServerRepository
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Movie Presenter.
 */
class MoviePresenter @Inject constructor(
    private val mainRepository: MainRepository,
    private val serverRepository: ServerRepository,
){

    private val movieId: Int = 0
    var view: MovieView? = null

    fun attachView(view: MovieView) {
        this.view = view
    }

    fun loadMovie(id: Int) {
        loadingMovies()
        checkSafeness()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                val movie = serverRepository.getMovie(id)
                if (movie.title.isEmpty()) showError()
                else showMovies(movie)
            }
        }
    }

    fun loadMovie() {
        loadingMovies()
        checkSafeness()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                val movie = serverRepository.getMovie(movieId)
                if (movie.title.isEmpty()) showError()
                else showMovies(movie)
            }
        }
    }

    fun saveMovie(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                mainRepository.addMovie(Movie(movieId, name))
            }
        }
        checkSafeness()
    }

    fun deleteMovie(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                mainRepository.deleteMovie(Movie(movieId, name))
            }
        }
        checkSafeness()
    }

    private fun checkSafeness() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                val movies = mainRepository.getAllMovies()
                view?.checkMovie(movies.find { it.id == movieId } != null)
            }
        }
    }

    fun detachView() {
        this.view = null
    }

    private fun loadingMovies() {
        view?.showLoading()
    }

    private fun showMovies(movie: ApiMovie) {
        view?.hideLoading()
        view?.showMovie(movie)
    }
    private fun showError() {
        view?.hideLoading()
        view?.showConnectionError()
    }

    interface MovieView {
        fun showMovie(movie: ApiMovie)
        fun checkMovie(isSaved: Boolean)
        fun showLoading()
        fun hideLoading()
        fun showConnectionError()
    }
}