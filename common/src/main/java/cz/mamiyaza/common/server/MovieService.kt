package cz.mamiyaza.common.server

import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.model.ApiMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Description of the API Queries.
 */
interface MovieService {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
    ): Response<ApiMovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
    ): Response<ApiMovie>
}