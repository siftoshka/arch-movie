package cz.mamiyaza.common.repository

import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.data.MovieDAO
import javax.inject.Inject

/**
 * The Main Repository of the App. Both for Remote and Local.
 */
class MainRepository @Inject constructor(private val movieDAO: MovieDAO) {

    suspend fun addMovie(movie: Movie) = movieDAO.addMovie(movie)

    fun getMovies() = movieDAO.getMovies()

    suspend fun getAllMovies() = movieDAO.getAllMovies()

    suspend fun deleteMovie(movie: Movie) = movieDAO.deleteMovie(movie)

    suspend fun deleteAllMovies() = movieDAO.deleteAllMovies()
}