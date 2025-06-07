package fct.nova.beeppisca.storage

import fct.nova.beeppisca.domain.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TicketRepository {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: Flow<List<Ticket>> = _tickets

    fun addTicket(ticket: Ticket) {
        _tickets.value = _tickets.value + ticket
    }

    fun getTickets(): List<Ticket> {
        return _tickets.value
    }
}
