package cz.mamiyaza.amovie.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.repository.MainRepository
import cz.mamiyaza.common.repository.ServerRepository
import cz.mamiyaza.common.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TODO add class description
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val serverRepository: ServerRepository) : ViewModel() {

    private val state = MutableLiveState<List<ApiMovieLite>>()

    val loading: LiveData<Boolean> = state.mapLoading()
    val error: LiveData<Boolean> = state.mapError()
    val data: LiveData<List<ApiMovieLite>> = state.mapLoaded().mapNotNull { it }

    fun getMovies() {
        state.loading()
        viewModelScope.launch {
            when(val result = wrapResult { serverRepository.getMovies(1) }) {
                is Result.success -> state.loaded(result.value.results)
                is Result.failure -> state.error(result.error)
            }
        }
    }
}