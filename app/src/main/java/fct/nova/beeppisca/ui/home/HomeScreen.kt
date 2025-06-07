package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.storage.TicketRepository

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Welcome to BeepPisca")

        Button(onClick = { /* Navigate to My Tickets screen */ }) {
            Text(text = "My Tickets")
        }

        Button(onClick = { /* Navigate to Purchase Ticket screen */ }) {
            Text(text = "Purchase Ticket")
        }

        Button(onClick = { /* Navigate to Bus Stops screen */ }) {
            Text(text = "Validate Ticket")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = HomeScreenViewModel(TicketRepository())) // Using a mock repository
}