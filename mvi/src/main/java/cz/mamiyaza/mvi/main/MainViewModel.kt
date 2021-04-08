package cz.mamiyaza.mvi.main

import androidx.lifecycle.*
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.repository.ServerRepository
import cz.mamiyaza.common.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main ViewModel.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val serverRepository: ServerRepository) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<ApiMovieLite>>> = MutableLiveData()
    private val _moreDataState: MutableLiveData<DataState<List<ApiMovieLite>>> = MutableLiveData()
    var page: Int = 1

    val dataState: LiveData<DataState<List<ApiMovieLite>>>
        get() = _dataState

    val moreDataState: LiveData<DataState<List<ApiMovieLite>>>
        get() = _moreDataState

    fun setStateEvent(mainStateEvent: MainStateEvent){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetMoviesEvent -> {
                    serverRepository.getMoviesFlow(1)
                        .onEach {
                            _dataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.GetMoreMovies -> {
                    serverRepository.getMoviesFlow(page)
                        .onEach {
                            _moreDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class MainStateEvent{

    object GetMoviesEvent: MainStateEvent()

    object GetMoreMovies: MainStateEvent()
}