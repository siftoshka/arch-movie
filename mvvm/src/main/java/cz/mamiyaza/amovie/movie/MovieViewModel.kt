package cz.mamiyaza.amovie.movie

import androidx.lifecycle.*
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.repository.MainRepository
import cz.mamiyaza.common.repository.ServerRepository
import cz.mamiyaza.common.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Movie ViewModel.
 */
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val serverRepository: ServerRepository,
    ) : ViewModel() {

    var movieId: Int = -1

    private val state = MutableLiveState<ApiMovie>()
    private val saveState = MutableLiveState<Boolean>()

    val savedMovies = mainRepository.getMovies().asLiveData(viewModelScope.coroutineContext)

    val loading: LiveData<Boolean> = state.mapLoading()
    val error: LiveData<Boolean> = state.mapError()
    val data: LiveData<ApiMovie> = state.mapLoaded().mapNotNull { it }

    fun requestItemDetails() {
        if (movieId == -1) return
    }

    fun getMovie() {
        state.loading()
        viewModelScope.launch {
            when(val result = wrapResult { serverRepository.getMovie(movieId) }) {
                is Result.success -> state.loaded(result.value)
                is Result.failure -> state.error(result.error)
            }
        }
    }

    fun saveMovie(name: String) {
        viewModelScope.launch {
            mainRepository.addMovie(Movie(movieId, name))
        }
    }

    fun deleteMovie(name: String) {
        viewModelScope.launch {
            mainRepository.deleteMovie(Movie(movieId, name))
        }
    }
}