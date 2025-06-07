package fct.nova.beeppisca.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.Ticket
import fct.nova.beeppisca.storage.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val ticketRepository: TicketRepository) : ViewModel() {

    private val _userTickets = MutableStateFlow<List<Ticket>>(emptyList())
    val userTickets: StateFlow<List<Ticket>> = _userTickets

    init {
        // Load tickets from the repository or local storage
        loadUserTickets()
    }

    private fun loadUserTickets() {
        viewModelScope.launch {
            _userTickets.value = ticketRepository.getTickets()
        }
    }
}
