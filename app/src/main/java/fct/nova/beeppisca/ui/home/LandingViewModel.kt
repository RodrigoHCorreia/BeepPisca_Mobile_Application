package fct.nova.beeppisca.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.MomentLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class LandingViewModel : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        object NoStop : UiState()
        data class AtStop(val stop: BusStop, val ticketType: TicketType) : UiState()
    }
    enum class TicketType { MONTHLY, REGULAR, NONE }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    /**
     * Simulates the 3 API calls:
     *  1) user loc → optional BusStop
     *  2) userId → hasMonthlyTicket
     *  3) userId → hasRegularTicket
     */
    fun load(userId: String, userLoc: MomentLocation) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            //TODO: replace with real API calls

            // 1) mock “find a bus stop” half the time
            val stop = if (Random.nextBoolean()) {
                BusStop(
                    id = "bs123",
                    name = "Central",
                    location = userLoc.toLocation(),
                    imageUrl = "" // TODO: replace with real URL or local drawable
                )
            } else null

            if (stop == null) {
                _uiState.value = UiState.NoStop
                return@launch
            }

            // 2) mock monthly
            if (Random.nextBoolean()) {
                _uiState.value = UiState.AtStop(stop, TicketType.MONTHLY)
                return@launch
            }
            // 3) mock regular
            if (Random.nextBoolean()) {
                _uiState.value = UiState.AtStop(stop, TicketType.REGULAR)
            } else {
                _uiState.value = UiState.AtStop(stop, TicketType.NONE)
            }
        }
    }
}
