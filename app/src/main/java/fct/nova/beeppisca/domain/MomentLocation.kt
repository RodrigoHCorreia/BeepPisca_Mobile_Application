package fct.nova.beeppisca.domain

data class MomentLocation(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
) {
    fun toLocation(): Location = Location(latitude, longitude)
}
