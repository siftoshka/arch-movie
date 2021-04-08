package cz.mamiyaza.mvi.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.repository.ServerRepository
import cz.mamiyaza.common.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search ViewModel.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<ApiMovieLite>>> = MutableLiveData()
    var query: String = ""

    val dataState: LiveData<DataState<List<ApiMovieLite>>>
        get() = _dataState


    fun setStateEvent(savedStateEvent: SavedStateEvent){
        viewModelScope.launch {
            when(savedStateEvent){
                is SavedStateEvent.GetSearchEvent -> {
                    delay(500)
                    serverRepository.makeSearchFlow(query)
                        .onEach {
                            _dataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class SavedStateEvent{

    object GetSearchEvent: SavedStateEvent()
}