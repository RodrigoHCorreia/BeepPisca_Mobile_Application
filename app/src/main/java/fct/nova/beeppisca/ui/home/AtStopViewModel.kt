package fct.nova.beeppisca.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.storage.BusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Own VM for the At-Stop screen.
 */
open class AtStopViewModel(
    private val busRepository: BusRepository = BusRepository()
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        object NoStop  : UiState()
        data class AtStop(val stop: BusStop, val ticketType: TicketType) : UiState()
    }

    enum class TicketType { MONTHLY, REGULAR, NONE }

    // 1) Backing state that never changes reference
    protected val _uiState = MutableStateFlow<UiState>(UiState.Loading)

    // 2) Expose as an open val so previews can override it
    open val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /** Call once you know userId & location to populate `uiState`. */
    fun loadForLocation(userId: String, loc: MomentLocation) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val stop = busRepository.getBusStopForLocation(loc.latitude, loc.longitude)
            _uiState.value =
                if (stop == null) UiState.NoStop
                else {
                    val hasMonthly = busRepository.hasMonthlyTicket(userId)
                    when {
                        hasMonthly -> UiState.AtStop(stop, TicketType.MONTHLY)
                        busRepository.hasRegularTicket(userId) -> UiState.AtStop(stop, TicketType.REGULAR)
                        else -> UiState.AtStop(stop, TicketType.NONE)
                    }
                }
        }
    }
}
