package fct.nova.beeppisca.ui.validation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ValidationScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Validate Your Ticket")

        Button(onClick = { /* Validate ticket logic */ }) {
            Text(text = "Validate Ticket")
        }

        Button(onClick = { /* Show QR code scanner */ }) {
            Text(text = "Scan QR Code")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValidationScreenPreview() {
    ValidationScreen() // Use mock data or default state for preview
}