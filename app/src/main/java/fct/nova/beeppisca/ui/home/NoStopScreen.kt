package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.domain.Ticket.MonthlyTicket
import fct.nova.beeppisca.domain.Ticket.StandaloneTicket
import fct.nova.beeppisca.ui.components.DefaultButton
import fct.nova.beeppisca.ui.theme.CarrisBlack
import fct.nova.beeppisca.ui.theme.CarrisBlue
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoStopScreen(
    viewModel: NoStopViewModel,
    userLoc: MomentLocation,
    userId: String?,
    onBuy: () -> Unit,
    onSearch: (String) -> Unit,
    onSettings: () -> Unit
) {
    val tickets by viewModel.userTickets.collectAsState()
    val query   by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.beeppisca),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.height(100.dp)
                    )
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = CarrisBlue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text(stringResource(R.string.destino)) },
                trailingIcon = {
                    IconButton(onClick = { onSearch(query) }) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.bilhetes_disponiveis),
                style = MaterialTheme.typography.headlineSmall,
                color = CarrisBlue
            )
            Spacer(Modifier.height(12.dp))

            if (tickets.isNotEmpty()) {
                TicketCarousel(tickets = tickets) { /* no-op */ }
            } else {
                Text(
                    text = stringResource(R.string.no_tickets_available),
                    style = MaterialTheme.typography.bodyLarge,
                    color = CarrisBlack
                )
            }

            Spacer(Modifier.height(24.dp))

            // narrower, centered buy button:
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                DefaultButton(
                    text = stringResource(R.string.button_buy_ticket),
                    onClick = onBuy,
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoStopContentPreview() {
    NoStopScreen(
        viewModel = NoStopViewModel(),
        userLoc = MomentLocation(0.0, 0.0, 0L),
        userId = null,
        onBuy = {},
        onSearch = {},
        onSettings = {}
    )
}

@Preview(showBackground = true)
@Composable
fun NoStopContentPreviewWithTickets() {
    val fakeVm = remember {
        object : NoStopViewModel() {
            override val userTickets = MutableStateFlow(
                listOf(
                    StandaloneTicket(
                        id = "ticket1",
                        dateOfPurchase = "2023-10-01T12:00:00Z"
                    ),
                    MonthlyTicket(
                        id = "ticket2",
                        dateOfPurchase = "2023-10-01T12:00:00Z",
                        expirationDate = "2023-11-01T12:00:00Z"
                    )
                )
            )
        }
    }

    NoStopScreen(
        viewModel = fakeVm,
        userLoc   = MomentLocation(0.0, 0.0, 0L),
        userId    = "preview_user",
        onBuy     = {},
        onSearch  = {},
        onSettings = { /* no-op */ }
    )
}
