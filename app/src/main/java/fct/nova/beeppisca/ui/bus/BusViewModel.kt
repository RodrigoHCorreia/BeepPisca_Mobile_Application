package fct.nova.beeppisca.ui.bus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusViewModel : ViewModel() {

    private val _busStops = MutableStateFlow<List<BusStop>>(emptyList())
    val busStops: StateFlow<List<BusStop>> = _busStops

    init {
        // Load bus stops (could be from a repository)
        loadBusStops()
    }

    private fun loadBusStops() {
        viewModelScope.launch {
            // Mock bus stop data for now
            _busStops.value = listOf(
                BusStop(id = "1", name = "Main Street Stop", location = Location(latitude = 40.7128, longitude = -74.0060), radius = 100.0),
                BusStop(id = "2", name = "Elm Street Stop", location = Location(latitude = 40.7138, longitude = -74.0070), radius = 50.0),
            )
        }
    }

    fun onBusStopSelected(busStop: BusStop) {
        // Handle bus stop selection (e.g., navigate to details or update state)
        // This is a placeholder for actual logic
        println("Selected Bus Stop: ${busStop.name} at ${busStop.location.latitude}, ${busStop.location.longitude}")
    }
}
