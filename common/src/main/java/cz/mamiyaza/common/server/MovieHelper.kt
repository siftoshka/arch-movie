package cz.mamiyaza.common.server

import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.model.ApiMovieResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Helper for [MovieService].
 */
interface MovieHelper {

    suspend fun getTrendingMovies(page: Int): Response<ApiMovieResponse>

    suspend fun getMovie(movieId: Int): Response<ApiMovie>
}

class MovieHelperImpl @Inject constructor(private val movieService: MovieService): MovieHelper {

    override suspend fun getTrendingMovies(page: Int): Response<ApiMovieResponse> = movieService.getTrendingMovies(page)

    override suspend fun getMovie(movieId: Int): Response<ApiMovie> = movieService.getMovie(movieId)
}