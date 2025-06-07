package fct.nova.beeppisca.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.*
import fct.nova.beeppisca.storage.BusRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    private val geoService: GeocodingService = MockGeocodingService,
    private val busRepository: BusRepository = BusRepository()
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun onQueryChange(new: String) {
        _query.value = new
    }

    fun performSearch(userLoc: MomentLocation) = viewModelScope.launch {
        val address = query.value.trim()
        if (address.isEmpty()) return@launch

        _loading.value = true
        _results.value = emptyList()

        val dest = geoService.geocodeAddress(address)
        if (dest != null) {
            val opts = busRepository.getBusOptions(userLoc, dest)
            _results.value = opts
        }
        _loading.value = false
    }
}
