package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import fct.nova.beeppisca.domain.SearchResult
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.ui.theme.CarrisBlack
import fct.nova.beeppisca.ui.theme.CarrisBlue

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    userLoc: MomentLocation,
    onSelect: (SearchResult) -> Unit
) {
    val query   = viewModel.query.collectAsState().value
    val results = viewModel.results.collectAsState().value
    val loading = viewModel.loading.collectAsState().value

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            label = { Text("Destino") },
            trailingIcon = {
                IconButton(onClick = { viewModel.performSearch(userLoc) }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        if (loading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CarrisBlue)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results.size) { idx ->
                    val item = results[idx]
                    SearchResultCard(item, onClick = { onSelect(item) })
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(item: SearchResult, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "${item.busNr}: ${item.origin} → ${item.destination}",
                style = MaterialTheme.typography.titleMedium,
                color = CarrisBlue
            )
            Spacer(Modifier.height(8.dp))
            // Stop name + walk distance
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = fct.nova.beeppisca.R.drawable.stickman),
                    contentDescription = null,
                    tint = CarrisBlack
                )
                Spacer(Modifier.width(4.dp))
                Text("${item.busStopName} • ${item.walkDistanceMeters} m",
                    style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(4.dp))
            // Bus distance
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = fct.nova.beeppisca.R.drawable.bus),
                    contentDescription = null,
                    tint = CarrisBlack
                )
                Spacer(Modifier.width(4.dp))
                Text("${item.busDistanceMeters} m",
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
