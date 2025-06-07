package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.Location
import fct.nova.beeppisca.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    viewModel: LandingViewModel,
    onValidate: () -> Unit,
    onBuy: () -> Unit,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name), color = CarrisBlack) },
                navigationIcon = {
                    if (uiState is LandingViewModel.UiState.AtStop &&
                        uiState.ticketType != LandingViewModel.TicketType.NONE
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
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
                            .width(130.dp)
                            .aspectRatio(1029f / 933f)
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = CarrisBlue,
                    titleContentColor = CarrisBlack,
                    actionIconContentColor = CarrisBlue,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // BUS-STOP CARD
            if (uiState is LandingViewModel.UiState.AtStop) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.paragem_title, uiState.stop.name),
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
                                        bottomEnd = 16.dp
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TICKET / ACTION CARD
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    when (uiState) {
                        LandingViewModel.UiState.Loading ->
                            CircularProgressIndicator(color = CarrisBlue)

                        LandingViewModel.UiState.NoStop ->
                            Text(
                                text = stringResource(R.string.no_stop_found),
                                style = MaterialTheme.typography.bodyLarge,
                                color = CarrisBlack
                            )

                        is LandingViewModel.UiState.AtStop -> {
                            when (uiState.ticketType) {
                                LandingViewModel.TicketType.MONTHLY ->
                                    TicketSection(
                                        imageRes = R.drawable.monthly_ticket,
                                        infoText = stringResource(R.string.monthly_ticket_info),
                                        buttonText = stringResource(R.string.button_validate_ticket),
                                        onButton = onValidate
                                    )
                                LandingViewModel.TicketType.REGULAR ->
                                    TicketSection(
                                        imageRes = R.drawable.regular_ticket,
                                        infoText = stringResource(R.string.regular_ticket_info),
                                        buttonText = stringResource(R.string.button_validate_ticket),
                                        onButton = onValidate
                                    )
                                LandingViewModel.TicketType.NONE ->
                                    DefaultButton(
                                        text = stringResource(R.string.button_buy_ticket),
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = infoText,
            style = MaterialTheme.typography.bodyMedium,
            color = CarrisBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
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
            onBack = {}
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
            onBack = {}
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
                _uiState.value = LandingViewModel.UiState.AtStop(sampleStop, LandingViewModel.TicketType.MONTHLY)
            },
            onValidate = {},
            onBuy = {},
            onBack = {}
        )
    }
}

@Preview(name = "At Stop - Regular", showBackground = true)
@Composable
fun LandingPreviewRegular() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
                _uiState.value = LandingViewModel.UiState.AtStop(sampleStop, LandingViewModel.TicketType.REGULAR)
            },
            onValidate = {},
            onBuy = {},
            onBack = {}
        )
    }
}

@Preview(name = "At Stop - None", showBackground = true)
@Composable
fun LandingPreviewNone() {
    BeepPiscaTheme {
        LandingScreen(
            viewModel = LandingViewModel().apply {
                _uiState.value = LandingViewModel.UiState.AtStop(sampleStop, LandingViewModel.TicketType.NONE)
            },
            onValidate = {},
            onBuy = {},
            onBack = {}
        )
    }
}
