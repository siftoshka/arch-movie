package cz.mamiyaza.mvi.movie

import androidx.lifecycle.*
import cz.mamiyaza.common.data.Movie
import cz.mamiyaza.common.model.ApiMovie
import cz.mamiyaza.common.repository.MainRepository
import cz.mamiyaza.common.repository.ServerRepository
import cz.mamiyaza.common.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _dataState: MutableLiveData<DataState<ApiMovie>> = MutableLiveData()
    var movieId: Int = -1

    val dataState: LiveData<DataState<ApiMovie>>
        get() = _dataState

    val savedMovies = mainRepository.getMovies().asLiveData(viewModelScope.coroutineContext)

    fun setStateEvent(movieStateEvent: MovieStateEvent){
        viewModelScope.launch {
            when(movieStateEvent){
                is MovieStateEvent.GetMovieEvent -> {
                    serverRepository.getMovieFlow(movieId)
                        .onEach {
                            _dataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
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

sealed class MovieStateEvent{

    object GetMovieEvent: MovieStateEvent()
}
