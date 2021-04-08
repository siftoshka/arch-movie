package cz.mamiyaza.common.repository

import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.server.MovieService
import cz.mamiyaza.common.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Server repository to get data from server.
 */
class ServerRepository @Inject constructor(private val service: MovieService) {

    suspend fun getMovies(page: Int) = service.getTrendingMovies(page)

    suspend fun getMovie(id: Int) = service.getMovie(id)

    suspend fun makeSearch(query: String, page: Int) = service.getMovieSearch(query, page)

    suspend fun getMoviesFlow(page: Int): Flow<DataState<List<ApiMovieLite>>> = flow {
        emit(DataState.Loading)
        try {
            emit(DataState.Success(service.getTrendingMovies(page).results))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}