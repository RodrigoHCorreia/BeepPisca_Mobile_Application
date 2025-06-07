package fct.nova.beeppisca.domain

/**
 * Represents one bus‐to‐destination option in search results.
 */
data class SearchResult(
    val busNr: String,
    val origin: String,
    val destination: String,
    val busStopName: String,
    val walkDistanceMeters: Int,
    val busDistanceMeters: Int
)