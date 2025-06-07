package fct.nova.beeppisca.storage

import fct.nova.beeppisca.domain.Bus
import fct.nova.beeppisca.domain.BusStop
import fct.nova.beeppisca.domain.Location
import fct.nova.beeppisca.domain.MomentLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalTime

class BusRepository {

    private val _buses = MutableStateFlow<List<Bus>>(emptyList())
    val buses: Flow<List<Bus>> = _buses

    fun getBusStops(busNumber: String): List<BusStop> {
        // Mock bus data for now
        val busStop1 = BusStop(
            id = "1",
            name = "Main Street Stop",
            location = Location(latitude = 40.7128, longitude = -74.0060),
            radius = 100.0
        )
        val busStop2 = BusStop(
            id = "2",
            name = "Elm Street Stop",
            location = Location(latitude = 40.7138, longitude = -74.0070),
            radius = 50.0
        )

        return if (busNumber == "102") {
            listOf(busStop1, busStop2)
        } else {
            emptyList()
        }
    }

    fun loadBuses() {
        // Mock data to populate buses
        val bus = Bus(
            busNumber = "750",
            origin = BusStop(
                id = "1",
                name = "Central Station",
                location = Location(latitude = 40.7128, longitude = -74.0060),
                radius = 100.0
            ),
            destination = BusStop(
                id = "2",
                name = "Downtown",
                location = Location(latitude = 40.7138, longitude = -74.0070),
                radius = 50.0
            ),
            stops = mapOf(
                BusStop(
                    id = "1",
                    name = "Central Station",
                    location = Location(latitude = 40.7128, longitude = -74.0060),
                    radius = 100.0
                ) to listOf(LocalTime.of(10, 0), LocalTime.of(10, 15)),
                BusStop(
                    id = "2",
                    name = "Downtown",
                    location = Location(latitude = 40.7138, longitude = -74.0070),
                    radius = 50.0
                ) to listOf(LocalTime.of(10, 30), LocalTime.of(10, 45))
            ),
            location = MomentLocation(
                latitude = 40.7128,
                longitude = -74.0060,
                timestamp = LocalTime.now().toNanoOfDay()
            ),
            capacity = 50,
        )
        _buses.value = listOf(bus)
    }
}
