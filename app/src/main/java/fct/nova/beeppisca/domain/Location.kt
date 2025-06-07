package fct.nova.beeppisca.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    /**
     * Calculates the great-circle distance between two points on the Earth's surface
     * using the Haversine formula.
     *
     * @param other the other [Location] to calculate the distance to
     * @return The distance between the two points in meters
     */
    fun haversineDistance(other: Location): Double {
        val earthRadius = 6_371_000.0  // meters

        val phi1 = Math.toRadians(latitude)
        val phi2 = Math.toRadians(other.latitude)
        val deltaPhi = Math.toRadians(other.latitude - latitude)
        val deltaLambda = Math.toRadians(other.longitude - longitude)

        val halfChordLengthSquared = sin(deltaPhi / 2).pow(2.0) +
                cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2.0)

        val angularDistance = 2 * atan2(sqrt(halfChordLengthSquared), sqrt(1 - halfChordLengthSquared))

        return earthRadius * angularDistance
    }
}