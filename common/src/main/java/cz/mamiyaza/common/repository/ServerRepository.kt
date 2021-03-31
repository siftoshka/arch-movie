package cz.mamiyaza.common.repository

import cz.mamiyaza.common.server.MovieService
import javax.inject.Inject

/**
 * Server repository to get data from server.
 */
class ServerRepository @Inject constructor(private val service: MovieService) {

    suspend fun getMovies(page: Int) = service.getTrendingMovies(page)

    suspend fun getMovie(id: Int) = service.getMovie(id)

    suspend fun makeSearch(query: String, page: Int) = service.getMovieSearch(query, page)
}