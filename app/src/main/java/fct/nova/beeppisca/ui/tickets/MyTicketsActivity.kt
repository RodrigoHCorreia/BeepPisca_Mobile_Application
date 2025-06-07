package fct.nova.beeppisca.ui.tickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class MyTicketsActivity : ComponentActivity() {

    private val ticketViewModel: TicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeepPiscaTheme {
                TicketScreen(tickets = ticketViewModel.tickets.value)
            }
        }
    }
}
