package fct.nova.beeppisca.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MomentLocation(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
) : Parcelable {
    fun toLocation(): Location = Location(latitude, longitude)
}
