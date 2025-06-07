package fct.nova.beeppisca.domain

import java.time.LocalTime

data class Bus(
    val busNumber: String,
    val origin: BusStop,
    val destination: BusStop,
    val stops: Map<BusStop, List<LocalTime>>,
    val location: MomentLocation,
    val capacity: Int,
    val occupancy: Int = 0,
    val payedOccupancy: Int = 0,
) {

    val illegalOccupancy: Int = if (occupancy > payedOccupancy) {
        occupancy - payedOccupancy
    } else {
        0
    }
}
