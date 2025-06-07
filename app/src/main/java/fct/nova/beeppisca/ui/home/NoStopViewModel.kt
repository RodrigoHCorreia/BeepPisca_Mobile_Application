package fct.nova.beeppisca.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.MockGeocodingService
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.domain.SearchResult
import fct.nova.beeppisca.domain.Ticket
import fct.nova.beeppisca.storage.BusRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class NoStopViewModel(
    private val repo: BusRepository = BusRepository()
) : ViewModel() {

    // 1) Backing MutableStateFlow that never changes reference
    protected val _userTickets = MutableStateFlow<List<Ticket>>(emptyList())

    /**
     * 2) Expose as a stable Flow reference so Compose won't see it as "new" on each recomposition
     *    Marked `open` so previews can override the entire Flow if desired.
     */
    open val userTickets: StateFlow<List<Ticket>> = _userTickets.asStateFlow()

    /** Load real tickets (or in tests, previews override `userTickets` directly) */
    fun loadUserTickets(userId: String) {
        viewModelScope.launch {
            _userTickets.value = repo.getUserTickets(userId)
        }
    }

    // --- search flow unchanged ---
    private val _searchQuery    = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults  = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _searchLoading  = MutableStateFlow(false)
    val searchLoading: StateFlow<Boolean> = _searchLoading.asStateFlow()

    fun onSearchQueryChange(new: String) {
        _searchQuery.value = new
    }

    fun performSearch(userLoc: MomentLocation) {
        viewModelScope.launch {
            val q = _searchQuery.value.trim()
            if (q.isEmpty()) return@launch

            _searchLoading.value = true
            _searchResults.value = emptyList()

            val dest = MockGeocodingService.geocodeAddress(q)
            if (dest != null) {
                _searchResults.value = repo.getBusOptions(userLoc, dest)
            }

            _searchLoading.value = false
        }
    }
}
