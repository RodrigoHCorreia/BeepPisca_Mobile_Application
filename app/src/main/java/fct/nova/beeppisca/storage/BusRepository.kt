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

}
