package fct.nova.beeppisca.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.*
import fct.nova.beeppisca.storage.BusRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LandingViewModel(
    private val busRepository: BusRepository    = BusRepository(),
    private val geoService: GeocodingService = MockGeocodingService
) : ViewModel() {

    /*─── 1) BUS-STOP UI STATE ──────────────────────────────────────────────────*/

    sealed class UiState {
        object Loading : UiState()
        object NoStop  : UiState()
        data class AtStop(val stop: BusStop, val ticketType: TicketType) : UiState()
    }
    enum class TicketType { MONTHLY, REGULAR, NONE }

    internal val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadForLocation(userId: String, loc: MomentLocation) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            // 1) find nearest stop
            val stop = busRepository.getBusStopForLocation(loc.latitude, loc.longitude)
            if (stop == null) {
                _uiState.value = UiState.NoStop
                return@launch
            }

            // 2) inspect tickets
            val hasMonthly = busRepository.hasMonthlyTicket(userId)
            _uiState.value = when {
                hasMonthly -> UiState.AtStop(stop, TicketType.MONTHLY)
                busRepository.hasRegularTicket(userId) -> UiState.AtStop(stop, TicketType.REGULAR)
                else -> UiState.AtStop(stop, TicketType.NONE)
            }
        }
    }

    /*─── 2) USER TICKETS CAROUSEL ──────────────────────────────────────────────*/

    private val _userTickets = MutableStateFlow<List<Ticket>>(emptyList())
    val userTickets: StateFlow<List<Ticket>> = _userTickets.asStateFlow()

    fun loadUserTickets(userId: String) {
        viewModelScope.launch {
            // replace with real API call
            _userTickets.value = busRepository.getUserTickets(userId)
        }
    }

    /*─── 3) SEARCH FLOW ───────────────────────────────────────────────────────*/

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _searchLoading = MutableStateFlow(false)
    val searchLoading: StateFlow<Boolean> = _searchLoading.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun performSearch(userLoc: MomentLocation) {
        viewModelScope.launch {
            val address = searchQuery.value.trim()
            if (address.isEmpty()) return@launch

            _searchLoading.value = true
            _searchResults.value = emptyList()

            // geocode
            val dest = geoService.geocodeAddress(address)
            if (dest != null) {
                // get bus options
                val opts = busRepository.getBusOptions(userLoc, dest)
                _searchResults.value = opts
            }

            _searchLoading.value = false
        }
    }
}
