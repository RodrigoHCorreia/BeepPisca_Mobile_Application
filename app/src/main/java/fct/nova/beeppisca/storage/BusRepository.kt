package fct.nova.beeppisca.storage

import fct.nova.beeppisca.domain.*
import fct.nova.beeppisca.domain.Ticket.MonthlyTicket
import fct.nova.beeppisca.domain.Ticket.StandaloneTicket
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * A single repository with all bus-related methods.
 * Currently fully mocked for prototyping.
 */
class BusRepository {

    /**
     * 50/50 chance to “find” a nearby stop.
     */
    suspend fun getBusStopForLocation(lat: Double, lon: Double): BusStop? {
        delay(300) // simulate network
        //return if (Random.nextBoolean()) {
            return BusStop(
                id       = "bs123",
                name     = "Central",
                location = Location(lat, lon),
                radius   = 100.0,
                imageUrl = "" // future: real URL
            )
        //} else null
    }

    /**
     * Randomly decide if the user has a monthly ticket.
     */
    suspend fun hasMonthlyTicket(userId: String): Boolean {
        delay(100)
        return true
        //return Random.nextBoolean()
    }

    /**
     * Randomly decide if the user has a regular (single-ride) ticket.
     */
    suspend fun hasRegularTicket(userId: String): Boolean {
        delay(100)
        return Random.nextBoolean()
    }

    /**
     * Return the list of tickets the user currently holds.
     * Excludes monthly if has none.
     */
    suspend fun getUserTickets(userId: String): List<Ticket> {
        delay(200)
        val list = mutableListOf<Ticket>()
        //if (Random.nextBoolean()) {
            list += MonthlyTicket(
                id                = "monthly123",
                dateOfPurchase   = "01/06",
                expirationDate   = "30/06",
            )
        //}
        list += StandaloneTicket(
            id                = "ticket123",
            dateOfPurchase   = "01/06",
            isUsed           = Random.nextBoolean(),
        )
        return list
    }

    /**
     * Given a user location and desired destination location,
     * produce a sorted list of SearchResult options.
     */
    suspend fun getBusOptions(
        userLoc: MomentLocation,
        destLocation: Location
    ): List<SearchResult> {
        delay(400)
        return (1..5).map { i ->
            SearchResult(
                busNr              = listOf("102", "205", "713", "28E", "760")[i % 5],
                origin             = "Paragem ${'A' + i}",
                destination        = "Destino X",
                busStopName        = "Stop ${i * 3}",
                walkDistanceMeters = Random.nextInt(50, 500),
                busDistanceMeters  = Random.nextInt(1000, 5000)
            )
        }.sortedBy { it.walkDistanceMeters }
    }

    fun getAllBuses(): List<Bus> {
        //delay(200)
        // six stops in Portugal
        val lisbon  = BusStop("LSB","Lisbon",  Location(38.7167, -9.1399),100.0)
        val porto   = BusStop("PT","Porto",    Location(41.1496, -8.6109),100.0)
        val coimbra = BusStop("CBR","Coimbra", Location(40.2033, -8.4103),100.0)
        val faro    = BusStop("FRO","Faro",    Location(37.0194, -7.9308),100.0)
        val braga   = BusStop("BRG","Braga",   Location(41.5454, -8.4265),100.0)
        val aveiro  = BusStop("AVR","Aveiro",  Location(40.6405, -8.6538),100.0)

        val now = System.currentTimeMillis()
        return listOf(
            Bus("102", lisbon,  porto,   emptyMap(), MomentLocation(38.7167, -9.1399, now), capacity=40, occupancy = 35, payedOccupancy = 32),
            Bus("205", porto,   coimbra, emptyMap(), MomentLocation(41.1496, -8.6109, now), capacity=30, occupancy= 28, payedOccupancy = 25),
            Bus("713", coimbra, faro,    emptyMap(), MomentLocation(40.2033, -8.4103, now), capacity=50, occupancy= 45, payedOccupancy = 40),
            Bus("28E", faro,    braga,   emptyMap(), MomentLocation(37.0194, -7.9308, now), capacity=30, occupancy= 30, payedOccupancy = 28),
            Bus("760", braga,   aveiro,  emptyMap(), MomentLocation(41.5454, -8.4265, now), capacity=25, occupancy=25, payedOccupancy = 25),
            Bus("550", aveiro,  lisbon,  emptyMap(), MomentLocation(40.6405, -8.6538, now), capacity=20, occupancy=10, payedOccupancy = 10),
        )
    }


}
