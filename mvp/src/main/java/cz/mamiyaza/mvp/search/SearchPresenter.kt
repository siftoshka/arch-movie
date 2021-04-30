package cz.mamiyaza.mvp.search

import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.repository.ServerRepository
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Search Presenter.
 */
class SearchPresenter @Inject constructor(
    private val serverRepository: ServerRepository
){

    var view: SearchView? = null

    fun attachView(view: SearchView) {
        this.view = view
    }

    fun searchMovies(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                delay(500)
                try {
                    if (query.isNotBlank()) {
                        val movies = serverRepository.makeSearch(query, 1)
                        showMovies(movies.results)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun detachView() {
        this.view = null
    }

    private fun showMovies(movies: List<ApiMovieLite>) {
        view?.showSearchList(movies)
    }

    interface SearchView {
        fun showSearchList(movies: List<ApiMovieLite>)
    }
}