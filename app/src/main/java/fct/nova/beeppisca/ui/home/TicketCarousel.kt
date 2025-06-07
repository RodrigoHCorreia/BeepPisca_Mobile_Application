package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.domain.Ticket
import fct.nova.beeppisca.ui.theme.*
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.toTicketType
import fct.nova.beeppisca.ui.components.ValidateButton

@Composable
fun TicketCarousel(
    tickets: List<Ticket>,
    validateCallback: () -> Unit
) {
    var idx by remember { mutableIntStateOf(0) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { idx = (idx - 1 + tickets.size) % tickets.size }) {
            Icon(painterResource(R.drawable.left_arrow_icon),
                contentDescription = stringResource(R.string.prev_ticket),
                tint = CarrisBlack,
                modifier = Modifier.size(60.dp))
        }
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                val ticket = tickets[idx]
                Image(
                    painter = painterResource(ticket.image),
                    contentDescription = stringResource(R.string.content_desc_ticket),
                    modifier = Modifier.size(200.dp)
                )
                if (ticket.type.toTicketType() == AtStopViewModel.TicketType.MONTHLY) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.valid_until, ticket.expirationDate.orEmpty()),
                        style = MaterialTheme.typography.bodySmall,
                        color = CarrisBlack
                    )
                }
                Spacer(Modifier.height(16.dp))
                ValidateButton(userId = null) // will be hidden, just spacing
            }
        }
        IconButton(onClick = { idx = (idx + 1) % tickets.size }) {
            Icon(painterResource(R.drawable.right_arrow_icon),
                contentDescription = stringResource(R.string.next_ticket),
                tint = CarrisBlack,
                modifier = Modifier.size(60.dp))
        }
    }
}