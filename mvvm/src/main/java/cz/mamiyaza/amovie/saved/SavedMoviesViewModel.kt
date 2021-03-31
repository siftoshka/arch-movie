package cz.mamiyaza.amovie.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Saved Movies ViewModel.
 */
@HiltViewModel
class SavedMoviesViewModel @Inject constructor(
    private val mainRepository: MainRepository
    ) : ViewModel() {

    val savedMovies = mainRepository.getMovies().asLiveData(viewModelScope.coroutineContext)

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            mainRepository.deleteMovie(movie)
        }
    }

    fun deleteMovies() {
        viewModelScope.launch {
            mainRepository.deleteAllMovies()
        }
    }
}