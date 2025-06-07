package fct.nova.beeppisca.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fct.nova.beeppisca.service.user.UserService
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Provide dependencies the same way you did for HomeActivity
        val api = UserService("https://localhost:8080")
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(api) as T
            }
        }
        val authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        setContent {
            BeepPiscaTheme {
                var loggedIn by remember { mutableStateOf(false) }
                if (!loggedIn) {
                    LoginScreenStateful(
                        viewModel = authViewModel,
                        onLoginSuccess = { loggedIn = true }
                    )
                } else {
                    // After login, navigate to Home or Settings
                    // For demo: just a message!
                    Text("Logged in! (Navigate to your Home/Settings screen here)")
                }
            }
        }
    }
}
