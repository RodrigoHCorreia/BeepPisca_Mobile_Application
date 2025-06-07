package fct.nova.beeppisca.domain

data class BusStop(
    val id: String,
    val name: String,
    val location: Location,
    val radius: Double = 10.0, // Default radius
    val imageUrl: String = ""             // URL for the stop’s image (new)
)
