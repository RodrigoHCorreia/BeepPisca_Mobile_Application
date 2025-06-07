// app/src/main/java/fct/nova/beeppisca/ui/adminView/AdminViewScreen.kt
package fct.nova.beeppisca.ui.adminView

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.Bus
import androidx.core.net.toUri

@Composable
fun AdminViewScreen(
    username: String,
    buses: List<Bus>,
    loading: Boolean
) {
    val ctx = LocalContext.current

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(R.string.hello_user, username),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            buses.forEach { bus ->
                BusCard(bus) { lat, lon ->
                    val uri = "geo:$lat,$lon?q=$lat,$lon".toUri()
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun BusCard(
    bus: Bus,
    onMapClick: (Double, Double) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Bus line + warning if needed
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.bus_number, bus.busNumber),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (bus.illegalOccupancy > 0) {
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(R.drawable.warning),
                            contentDescription = stringResource(R.string.illegal_warning),
                            tint = MaterialTheme.colorScheme.error,  // red by theme
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.route_format, bus.origin.name, bus.destination.name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (bus.illegalOccupancy > 0) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.illegal_count, bus.illegalOccupancy),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            IconButton(onClick = { onMapClick(bus.location.latitude, bus.location.longitude) }) {
                Icon(
                    painter = painterResource(R.drawable.location),
                    contentDescription = stringResource(R.string.open_in_maps),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
