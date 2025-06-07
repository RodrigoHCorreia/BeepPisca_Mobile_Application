package fct.nova.beeppisca.ui.bus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState

@Composable
fun BusScreen(viewModel: BusViewModel) {

    // Collect the bus stops from the ViewModel
    val busStops = viewModel.busStops.collectAsState(initial = emptyList()).value

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Nearby Bus Stops")
        LazyColumn {
            items(busStops.size) { index ->
                val busStop = busStops[index]
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Bus Stop: ${busStop.name}")
                    Text(text = "Location: ${busStop.location.latitude }, ${busStop.location.longitude}")
                    Button(onClick = { viewModel.onBusStopSelected(busStop) }) {
                        Text(text = "Select Bus Stop")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusScreenPreview() {
    BusScreen(viewModel = BusViewModel()) // Use mock data in the ViewModel
}
