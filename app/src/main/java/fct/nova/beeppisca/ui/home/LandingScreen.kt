package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.Location
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme
import fct.nova.beeppisca.ui.theme.DefaultButton

/**
 * Extracted content composable so we can preview all states.
 */
@Composable
fun LandingContent(
    uiState: LandingViewModel.UiState,
    onValidate: () -> Unit,
    onBuy: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            LandingViewModel.UiState.Loading -> {
                CircularProgressIndicator()
            }
            LandingViewModel.UiState.NoStop -> {
                Text(stringResource(R.string.no_stop_found))
            }
            is LandingViewModel.UiState.AtStop -> {
                val stop = uiState.stop
                val type = uiState.ticketType

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.paragem_title, stop.name),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.busstop_placeholder),
                        contentDescription = stringResource(R.string.content_desc_busstop),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(701f/326f) // aspect ratio of the placeholder image
                            .padding(horizontal = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(40.dp))
                    when (type) {
                        LandingViewModel.TicketType.MONTHLY -> {
                            Image(
                                painter = painterResource(R.drawable.monthly_ticket),
                                contentDescription = stringResource(R.string.content_desc_ticket),
                                modifier = Modifier.size(250.dp)
                            )
                            Spacer(Modifier.height(30.dp))
                            DefaultButton(
                                text = stringResource(R.string.button_validate_ticket),
                                onClick = onValidate,
                                modifier = Modifier.fillMaxWidth(0.8f),
                            )
                        }
                        LandingViewModel.TicketType.REGULAR -> {
                            Image(
                                painter = painterResource(R.drawable.regular_ticket),
                                contentDescription = stringResource(R.string.content_desc_ticket),
                                modifier = Modifier.size(250.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            DefaultButton(
                                text = stringResource(R.string.button_validate_ticket),
                                onClick = onValidate,
                                modifier = Modifier.fillMaxWidth(0.8f),
                            )
                        }
                        LandingViewModel.TicketType.NONE -> {
                            DefaultButton(
                                text = stringResource(R.string.button_buy_ticket),
                                onClick = onBuy,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * The runtime composable that hooks into the ViewModel.
 */
@Composable
fun LandingScreen(
    viewModel: LandingViewModel,
    onValidate: () -> Unit,
    onBuy: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    LandingContent(uiState, onValidate, onBuy)
}

/** Previews for each scenario **/

@Preview(name = "Loading State", showBackground = true)
@Composable
fun LandingPreviewLoading() {
    BeepPiscaTheme {
        LandingContent(
            uiState = LandingViewModel.UiState.Loading,
            onValidate = {},
            onBuy = {}
        )
    }
}

@Preview(name = "No Stop Found", showBackground = true)
@Composable
fun LandingPreviewNoStop() {
    BeepPiscaTheme {
        LandingContent(
            uiState = LandingViewModel.UiState.NoStop,
            onValidate = {},
            onBuy = {}
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
        LandingContent(
            uiState = LandingViewModel.UiState.AtStop(sampleStop, LandingViewModel.TicketType.MONTHLY),
            onValidate = {},
            onBuy = {}
        )
    }
}

@Preview(name = "At Stop - Regular", showBackground = true)
@Composable
fun LandingPreviewRegular() {
    BeepPiscaTheme {
        LandingContent(
            uiState = LandingViewModel.UiState.AtStop(sampleStop, LandingViewModel.TicketType.REGULAR),
            onValidate = {},
            onBuy = {}
        )
    }
}

@Preview(name = "At Stop - None", showBackground = true)
@Composable
fun LandingPreviewNone() {
    BeepPiscaTheme {
        LandingContent(
            uiState = LandingViewModel.UiState.AtStop(sampleStop, LandingViewModel.TicketType.NONE),
            onValidate = {},
            onBuy = {}
        )
    }
}
