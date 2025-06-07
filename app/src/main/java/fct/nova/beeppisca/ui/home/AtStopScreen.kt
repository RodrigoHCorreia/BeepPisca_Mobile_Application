package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.Location
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.ui.components.DefaultButton
import fct.nova.beeppisca.ui.theme.CarrisBlack
import fct.nova.beeppisca.ui.theme.CarrisBlue
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtStopScreen(
    viewModel: AtStopViewModel,
    userLoc: MomentLocation,
    userId: String?,
    onBack: () -> Unit,
    onBuy: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = CarrisBlue
                        )
                    }
                },
                title = {
                    Image(
                        painter = painterResource(R.drawable.beeppisca),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.height(100.dp)
                    )
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
        ) {
            Spacer(Modifier.height(24.dp))

            when (uiState) {
                AtStopViewModel.UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = CarrisBlue)
                    }
                }
                AtStopViewModel.UiState.NoStop -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_stop_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = CarrisBlack
                        )
                    }
                }
                is AtStopViewModel.UiState.AtStop -> {
                    val (stop, type) = uiState

                    // 1) Stop card
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
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
                                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // 2) Ticket card + validate / buy
                    when (type) {
                        AtStopViewModel.TicketType.MONTHLY,
                        AtStopViewModel.TicketType.REGULAR -> {
                            // show ticket image + info + Validate button
                            TicketSection(
                                imageRes = if (type == AtStopViewModel.TicketType.MONTHLY)
                                    R.drawable.monthly_ticket
                                else
                                    R.drawable.regular_ticket,
                                infoText = stringResource(
                                    if (type == AtStopViewModel.TicketType.MONTHLY)
                                        R.string.monthly_ticket_info
                                    else
                                        R.string.regular_ticket_info
                                ),
                                userId   = userId
                            )
                        }
                        AtStopViewModel.TicketType.NONE -> {
                            // only Buy button
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
            }
        }
    }
}


// sample stop for previews
private val sampleStop = BusStop(
    id = "bs123",
    name = "Central",
    location = Location(40.0, -74.0),
    radius = 100.0
)

private val sampleLoc = MomentLocation(40.0, -74.0, 0L)

/** 1) Loading Preview **/
@Preview(name = "AtStop - Loading", showBackground = true)
@Composable
fun AtStopPreviewLoading() {
    val vm = remember {
        object : AtStopViewModel() {
            override val uiState = MutableStateFlow(UiState.Loading)
        }
    }
    AtStopScreen(vm, sampleLoc, userId = "u1", onBack = {}, onBuy = {})
}

/** 2) NoStop Preview **/
@Preview(name = "AtStop - NoStop", showBackground = true)
@Composable
fun AtStopPreviewNoStop() {
    val vm = remember {
        object : AtStopViewModel() {
            override val uiState = MutableStateFlow(UiState.NoStop)
        }
    }
    AtStopScreen(vm, sampleLoc, userId = "u1", onBack = {}, onBuy = {})
}

/** 3) Monthly Ticket Preview **/
@Preview(name = "AtStop - Monthly", showBackground = true)
@Composable
fun AtStopPreviewMonthly() {
    val vm = remember {
        object : AtStopViewModel() {
            override val uiState = MutableStateFlow(
                UiState.AtStop(sampleStop, TicketType.MONTHLY)
            )
        }
    }
    AtStopScreen(vm, sampleLoc, userId = "u1", onBack = {}, onBuy = {})
}

/** 4) Regular Ticket Preview **/
@Preview(name = "AtStop - Regular", showBackground = true)
@Composable
fun AtStopPreviewRegular() {
    val vm = remember {
        object : AtStopViewModel() {
            override val uiState = MutableStateFlow(
                UiState.AtStop(sampleStop, TicketType.REGULAR)
            )
        }
    }
    AtStopScreen(vm, sampleLoc, userId = "u1", onBack = {}, onBuy = {})
}

/** 5) No Ticket Preview **/
@Preview(name = "AtStop - None", showBackground = true)
@Composable
fun AtStopPreviewNone() {
    val vm = remember {
        object : AtStopViewModel() {
            override val uiState = MutableStateFlow(
                UiState.AtStop(sampleStop, TicketType.NONE)
            )
        }
    }
    AtStopScreen(vm, sampleLoc, userId = "u1", onBack = {}, onBuy = {})
}