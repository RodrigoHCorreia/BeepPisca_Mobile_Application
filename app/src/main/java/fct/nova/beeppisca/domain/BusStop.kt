package fct.nova.beeppisca.domain

data class BusStop(
    val id: String,
    val name: String,
    val location: Location,
    val radius: Double = 10.0,
    val imageUrl: String = ""
)
