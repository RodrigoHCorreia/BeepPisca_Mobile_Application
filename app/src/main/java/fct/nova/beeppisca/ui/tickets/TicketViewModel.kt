package fct.nova.beeppisca.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketViewModel : ViewModel() {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    init {
        // Mock fetching tickets from repository or local storage
        loadTickets()
    }

    private fun loadTickets() {
        viewModelScope.launch {
            // Simulating fetching tickets
            _tickets.value = listOf(
                Ticket.StandaloneTicket(
                    id = "1",
                    dateOfPurchase = "2025-06-01",
                    expirationDate = "2025-06-10"
                ),
                Ticket.MonthlyTicket(
                    id = "2",
                    dateOfPurchase = "2025-06-01",
                    expirationDate = "2025-06-30"
                )
            )
        }
    }
}
