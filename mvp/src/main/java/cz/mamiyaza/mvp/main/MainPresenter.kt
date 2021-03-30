package cz.mamiyaza.mvp.main

import cz.mamiyaza.common.model.ApiMovieLite

/**
 * TODO add class description
 */
class MainPresenter {

    var view: MainView? = null

    fun attachView(view: MainView) {
        this.view = view

        loadMovies()
    }

    private fun loadMovies() {
        loadingMovies();

//        getMoviesUseCase.execute(
//            onResult = { result ->
//                result.fold({showError()}, {movies -> showMovies(movies)})
//            })
    }


    fun detachView() {
        this.view = null
    }

    private fun loadingMovies() {
        view?.showLoading()
        view?.clearMovies()
    }

    private fun showMovies(movies: List<ApiMovieLite>) {
        view?.hideLoading()
        view?.showMovieList(movies)
    }

    private fun showError() {
        view?.hideLoading()
        view?.showConnectionError()
    }

    interface MainView {
        fun showMovieList(movies: List<ApiMovieLite>)
        fun clearMovies()
        fun showLoading()
        fun hideLoading()
        fun showConnectionError()
    }
}