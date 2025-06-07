package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.Location
import fct.nova.beeppisca.domain.SearchResult
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.domain.toTicketType
import fct.nova.beeppisca.ui.home.LandingViewModel.TicketType
import fct.nova.beeppisca.ui.theme.*
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    viewModel: LandingViewModel,
    userLoc: MomentLocation,
    onValidate: () -> Unit,
    onBuy: () -> Unit,
    onBack: () -> Unit,
    onSearchSelect: (SearchResult) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is LandingViewModel.UiState.NoStop) {
            viewModel.loadUserTickets("CURRENT_USER_ID")
        }
    }
    var currentIdx by remember { mutableIntStateOf(0) }
    val userName = "Rodrigo" // TODO: get actual user name from preferences

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = CarrisBlack
                    )
                },
                navigationIcon = {
                    if (uiState is LandingViewModel.UiState.AtStop &&
                        (uiState as LandingViewModel.UiState.AtStop).ticketType != TicketType.NONE
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = CarrisBlue
                            )
                        }
                    }
                },
                actions = {
                    Image(
                        painter = painterResource(R.drawable.carris_logo),
                        contentDescription = stringResource(R.string.carris_logo_desc),
                        modifier = Modifier
                            .width(80.dp)
                            .aspectRatio(1029f / 933f)
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = CarrisBlue,
                    titleContentColor = CarrisBlack,
                    actionIconContentColor = CarrisBlue,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            when (uiState) {
                LandingViewModel.UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = CarrisBlue)
                    }
                }

                LandingViewModel.UiState.NoStop -> {
                    val query   by viewModel.searchQuery.collectAsState()
                    val loading by viewModel.searchLoading.collectAsState()
                    val results by viewModel.searchResults.collectAsState()
                    val tickets by viewModel.userTickets.collectAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // 1) Search bar
                        OutlinedTextField(
                            value = query,
                            onValueChange = viewModel::onSearchQueryChange,
                            label = { Text(stringResource(R.string.destino)) },
                            trailingIcon = {
                                IconButton(onClick = { viewModel.performSearch(userLoc) }) {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))

                        // 2) Tickets carousel
                        Text(
                            text = stringResource(R.string.bilhetes_disponiveis),
                            style = MaterialTheme.typography.headlineSmall,
                            color = CarrisBlue
                        )
                        Spacer(Modifier.height(12.dp))

                        if (tickets.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = { currentIdx = (currentIdx - 1 + tickets.size) % tickets.size }) {
                                    Icon(
                                        painter           = painterResource(id = R.drawable.left_arrow_icon),
                                        contentDescription= stringResource(R.string.prev_ticket),
                                        tint              = CarrisBlack,
                                        modifier          = Modifier.size(60.dp)
                                    )
                                }

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(24.dp)
                                    ) {
                                        val ticket = tickets[currentIdx]
                                        Image(
                                            painter = painterResource(ticket.image),
                                            contentDescription = stringResource(R.string.content_desc_ticket),
                                            modifier = Modifier.size(200.dp)
                                        )
                                        if (ticket.type.toTicketType() == TicketType.MONTHLY) {
                                            Spacer(Modifier.height(8.dp))
                                            Text(
                                                text = stringResource(
                                                    R.string.valid_until,
                                                    ticket.expirationDate.orEmpty()
                                                ),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = CarrisBlack
                                            )
                                        }
                                    }
                                }

                                IconButton(onClick = { currentIdx = (currentIdx + 1) % tickets.size }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.right_arrow_icon),
                                        contentDescription = stringResource(R.string.next_ticket),
                                        tint = CarrisBlack,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = stringResource(R.string.no_tickets_available),
                                style = MaterialTheme.typography.bodyLarge,
                                color = CarrisBlack
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // 3) Greeting + buy button
                        Text(
                            text = stringResource(R.string.hello_user, userName),
                            style = MaterialTheme.typography.titleMedium,
                            color = CarrisBlack,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        DefaultButton(
                            text = stringResource(R.string.button_buy_ticket),
                            onClick = onBuy,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }

                is LandingViewModel.UiState.AtStop -> {
                    val stop = (uiState as LandingViewModel.UiState.AtStop).stop
                    val type = (uiState as LandingViewModel.UiState.AtStop).ticketType

                    // 1) Bus-stop card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.paragem_title, stop.name),
                                style = MaterialTheme.typography.headlineSmall,
                                color = CarrisBlue,
                                modifier = Modifier.padding(16.dp)
                            )
                            Image(
                                painter = painterResource(R.drawable.busstop_placeholder),
                                contentDescription = stringResource(R.string.content_desc_busstop),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(701f / 326f)
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 16.dp,
                                            bottomEnd   = 16.dp
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // 2) Ticket/action card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            when (type) {
                                TicketType.MONTHLY ->
                                    TicketSection(
                                        imageRes   = R.drawable.monthly_ticket,
                                        infoText   = stringResource(R.string.monthly_ticket_info),
                                        buttonText = stringResource(R.string.button_validate_ticket),
                                        onButton   = onValidate
                                    )

                                TicketType.REGULAR ->
                                    TicketSection(
                                        imageRes   = R.drawable.regular_ticket,
                                        infoText   = stringResource(R.string.regular_ticket_info),
                                        buttonText = stringResource(R.string.button_validate_ticket),
                                        onButton   = onValidate
                                    )

                                TicketType.NONE ->
                                    DefaultButton(
                                        text    = stringResource(R.string.button_buy_ticket),
                                        onClick = onBuy
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketSection(
    imageRes: Int,
    infoText: String,
    buttonText: String,
    onButton: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(R.string.content_desc_ticket),
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text      = infoText,
            style     = MaterialTheme.typography.bodyMedium,
            color     = CarrisBlack,
            textAlign = TextAlign.Center,
            modifier  = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        DefaultButton(text = buttonText, onClick = onButton)
    }
}



/** Previews for each scenario **/

@Preview(name = "Loading State", showBackground = true)
@Composable
fun LandingPreviewLoading() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
            },
            onValidate = {},
            onBuy = {},
            onBack = {},
            onSearchSelect = {},
            userLoc = MomentLocation(38.736946, -9.142685, 0L)
        )
    }
}

@Preview(name = "No Stop Found", showBackground = true)
@Composable
fun LandingPreviewNoStop() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
                // Simulate no stop found
                _uiState.value = LandingViewModel.UiState.NoStop
            },
            onValidate = {},
            onBuy = {},
            onBack = {},
            onSearchSelect = {},
            userLoc = MomentLocation(38.736946, -9.142685, 0L)
        )
    }
}

private val sampleStop = BusStop(
    id = "bs123",
    name = "Central",
    location = Location(40.0, -74.0),
    radius = 100.0,
    imageUrl = "" // placeholder drawable will show
)

@Preview(name = "At Stop - Monthly", showBackground = true)
@Composable
fun LandingPreviewMonthly() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
                _uiState.value = LandingViewModel.UiState.AtStop(sampleStop, TicketType.MONTHLY)
            },
            onValidate = {},
            onBuy = {},
            onBack = {},
            onSearchSelect = {},
            userLoc = MomentLocation(40.0, -74.0, LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC).toEpochMilli())
        )
    }
}

@Preview(name = "At Stop - Regular", showBackground = true)
@Composable
fun LandingPreviewRegular() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
                _uiState.value = LandingViewModel.UiState.AtStop(sampleStop, TicketType.REGULAR)
            },
            onValidate = {},
            onBuy = {},
            onBack = {},
            onSearchSelect = {},
            userLoc = MomentLocation(40.0, -74.0, LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC).toEpochMilli())
        )
    }
}

@Preview(name = "At Stop - None", showBackground = true)
@Composable
fun LandingPreviewNone() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
                _uiState.value = LandingViewModel.UiState.AtStop(sampleStop, TicketType.NONE)
            },
            onValidate = {},
            onBuy = {},
            onBack = {},
            onSearchSelect = {},
            userLoc = MomentLocation(40.0, -74.0, LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC).toEpochMilli())
        )
    }
}
