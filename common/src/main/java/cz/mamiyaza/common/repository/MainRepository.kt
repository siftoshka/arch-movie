package cz.mamiyaza.common.repository

import cz.mamiyaza.common.server.MovieHelper
import javax.inject.Inject

/**
 * The Main Repository of the App. Both for Remote and Local.
 */
class MainRepository @Inject constructor(private val movieHelper: MovieHelper) {

    suspend fun getTrendingMovies(page: Int) = movieHelper.getTrendingMovies(page)

    suspend fun getMovie(movieId: Int) = movieHelper.getMovie(movieId)
}