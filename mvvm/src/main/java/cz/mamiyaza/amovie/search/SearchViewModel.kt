package cz.mamiyaza.amovie.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mamiyaza.common.model.ApiMovieLite
import cz.mamiyaza.common.repository.ServerRepository
import cz.mamiyaza.common.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search ViewModel.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val state = MutableLiveState<List<ApiMovieLite>>()

    val data: LiveData<List<ApiMovieLite>> = state.mapLoaded().mapNotNull { it }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            delay(500)
            when(val result = wrapResult { serverRepository.makeSearch(query, 1) }) {
                is Result.success -> state.loaded(result.value.results)
                is Result.failure -> state.error(result.error)
            }
        }
    }

}