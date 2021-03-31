package cz.mamiyaza.amovie.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
 * Main ViewModel.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val serverRepository: ServerRepository) : ViewModel() {

    private val state = MutableLiveState<List<ApiMovieLite>>()
    private val newState = MutableLiveState<List<ApiMovieLite>>()

    val loading: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(state.mapLoading()) { value = it }
        addSource(newState.mapLoading()) { value = it }
    }
    val error: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(state.mapError()) { value = it }
        addSource(newState.mapError()) { value = it }
    }
    val data: LiveData<List<ApiMovieLite>> = state.mapLoaded().mapNotNull { it }
    val moreData: LiveData<List<ApiMovieLite>> = newState.mapLoaded().mapNotNull { it }

    fun getMovies() {
        state.loading()
        viewModelScope.launch {
            when(val result = wrapResult { serverRepository.getMovies(1) }) {
                is Result.success -> state.loaded(result.value.results)
                is Result.failure -> state.error(result.error)
            }
        }
    }

    fun addMoreMovies(page: Int) {
        viewModelScope.launch {
            when(val result = wrapResult { serverRepository.getMovies(page) }) {
                is Result.success -> newState.loaded(result.value.results)
                is Result.failure -> newState.error(result.error)
            }
        }
    }
}