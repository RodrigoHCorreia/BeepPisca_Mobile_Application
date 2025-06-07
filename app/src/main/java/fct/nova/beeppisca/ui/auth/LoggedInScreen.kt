package fct.nova.beeppisca.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.domain.SimpleUser

@Composable
fun LoggedInScreen(
    user: SimpleUser?,
    onLogout: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Text("Welcome!")
        Spacer(Modifier.height(8.dp))
        user?.let {
            Text("Username: ${it.name}")
            Text("Email: ${it.email}")
            if (it.isAdmin) {
                Text("You are an admin.")
            }
        } ?: Text("Loading user details...")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onLogout) { Text("Logout") }
    }
}

// ----------- Previews -----------

@Preview(showBackground = true, name = "Logged In - User")
@Composable
fun PreviewLoggedInScreen_User() {
    LoggedInScreen(
        user = SimpleUser(
            name = "Carlos",
            email = "carlos@example.com",
            password = null,
            token = null,
            isAdmin = false
        ),
        onLogout = {}
    )
}

@Preview(showBackground = true, name = "Logged In - Admin")
@Composable
fun PreviewLoggedInScreen_Admin() {
    LoggedInScreen(
        user = SimpleUser(
            name = "AdminUser",
            email = "admin@company.com",
            password = null,
            token = null,
            isAdmin = true
        ),
        onLogout = {}
    )
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun PreviewLoggedInScreen_Loading() {
    LoggedInScreen(
        user = null,
        onLogout = {}
    )
}
