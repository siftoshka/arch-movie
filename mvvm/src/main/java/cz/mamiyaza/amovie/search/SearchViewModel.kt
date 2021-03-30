package cz.mamiyaza.amovie.search

import androidx.lifecycle.ViewModel
import cz.mamiyaza.common.repository.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * TODO add class description
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val serverRepository: ServerRepository
) : ViewModel() {


}