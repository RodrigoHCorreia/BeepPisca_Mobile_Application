package fct.nova.beeppisca.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Settings")

        // Dark/Light theme switch
        Text(text = "Dark Mode")
        Switch(checked = false, onCheckedChange = { isChecked ->
            if (isChecked) {
                // Handle enabling dark mode
            } else {
                // Handle disabling dark mode
            }
        })

        Button(onClick = { /* Handle saving preferences */ }) {
            Text(text = "Save Settings")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen() // Use mock data if necessary
}