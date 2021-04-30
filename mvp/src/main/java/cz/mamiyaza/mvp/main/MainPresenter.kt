package cz.mamiyaza.mvp.main

import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.repository.ServerRepository
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Main Presenter.
 */
class MainPresenter @Inject constructor(
    private val serverRepository: ServerRepository,
){

    var view: MainView? = null

    fun attachView(view: MainView) {
        this.view = view

        loadMovies()
    }

    fun loadMovies() {
        loadingMovies()

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                try {
                    val movies = serverRepository.getMovies(1)
                    if (movies.results.isEmpty()) showError()
                    else showMovies(movies.results)
                } catch (e: Exception) {
                    e.printStackTrace()
                    showError()
                }
            }
        }
    }

    fun addMoreMovies(page: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                try {
                    val movies = serverRepository.getMovies(page)
                    if (movies.results.isEmpty()) showError()
                    else showMoreMovies(movies.results)
                } catch (e: Exception) {
                    e.printStackTrace()
                    showError()
                }
            }
        }
    }

    fun detachView() {
        this.view = null
    }

    private fun loadingMovies() {
        view?.showLoading()
    }

    private fun showMovies(movies: List<ApiMovieLite>) {
        view?.hideLoading()
        view?.showMovieList(movies)
    }

    private fun showMoreMovies(movies: List<ApiMovieLite>) {
        view?.showMoreMovieList(movies)
    }

    private fun showError() {
        view?.hideLoading()
        view?.showConnectionError()
    }

    interface MainView {
        fun showMovieList(movies: List<ApiMovieLite>)
        fun showMoreMovieList(movies: List<ApiMovieLite>)
        fun showLoading()
        fun hideLoading()
        fun showConnectionError()
    }
}