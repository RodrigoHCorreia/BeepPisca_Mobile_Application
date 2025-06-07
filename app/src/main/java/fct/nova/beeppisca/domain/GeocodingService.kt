package fct.nova.beeppisca.domain

/**
 * Abstraction for turning an address string into coordinates.
 */
interface GeocodingService {
    suspend fun geocodeAddress(address: String): Location?
}

/**
 * Fake implementation for now: returns a random nearby point
 */
object MockGeocodingService : GeocodingService {
    override suspend fun geocodeAddress(address: String): Location? {
        return Location(
            latitude  = 38.736946 + (-0.02..0.02).random(),
            longitude = -9.142685 + (-0.02..0.02).random()
        )
    }

    private fun ClosedFloatingPointRange<Double>.random() =
        start + (endInclusive - start) * Math.random()
}