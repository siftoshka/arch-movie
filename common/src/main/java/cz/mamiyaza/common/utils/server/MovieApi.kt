package cz.mamiyaza.common.utils.server

import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.model.ApiMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Description of the API Queries.
 */
interface MovieApi {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("language") language: String?
    ): ApiMovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): ApiMovie
}