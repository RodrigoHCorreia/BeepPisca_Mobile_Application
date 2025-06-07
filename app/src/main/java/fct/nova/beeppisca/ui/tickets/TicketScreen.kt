package fct.nova.beeppisca.ui.tickets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.domain.Ticket

@Composable
fun TicketScreen(tickets: List<Ticket>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Your Tickets")

        tickets.forEach { ticket ->
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Ticket Type: ${ticket.type}")
                Text(text = "Expiration Date: ${ticket.expirationDate}")
                Button(onClick = { /* Handle ticket validation */ }) {
                    Text(text = "Validate Ticket")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketScreenPreview() {
    TicketScreen(
        tickets = listOf(
            Ticket.StandaloneTicket("1", dateOfPurchase = "2025-06-01", expirationDate = "2025-06-10"),
            Ticket.MonthlyTicket("2", dateOfPurchase = "2025-06-01", expirationDate = "2025-06-30")
        )
    )
}
