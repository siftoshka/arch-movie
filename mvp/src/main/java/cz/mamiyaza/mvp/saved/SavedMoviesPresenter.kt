package cz.mamiyaza.mvp.saved

import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.repository.MainRepository
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Saved Movies Presenter.
 */
class SavedMoviesPresenter @Inject constructor(
    private val mainRepository: MainRepository,
){

    var view: SavedMoviesView? = null
    private var localMovies: List<Movie> = emptyList()

    fun attachView(view: SavedMoviesView) {
        this.view = view

        loadMovies()
    }

    private fun loadMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                val movies = mainRepository.getAllMovies()
                localMovies = movies
                view?.showMovies(movies)
            }
        }
    }

    fun deleteMovie(movie: Movie) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                mainRepository.deleteMovie(movie)
                view?.updateView(localMovies - listOf(movie))
            }
        }
    }

    fun deleteMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                mainRepository.deleteAllMovies()
                view?.updateWholeView()
            }
        }
    }

    fun detachView() {
        this.view = null
    }

    interface SavedMoviesView {
        fun showMovies(movies: List<Movie>)
        fun updateView(movies: List<Movie>)
        fun updateWholeView()
    }
}