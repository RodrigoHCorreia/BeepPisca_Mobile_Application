package fct.nova.beeppisca.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.preferences.preferencesDataStore
import fct.nova.beeppisca.service.user.UserService
import fct.nova.beeppisca.storage.LocalDataStore
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class AuthActivity : ComponentActivity() {

    // Create a DataStore instance (one per app context)
    private val dataStore by preferencesDataStore(name = "beep_pisca_prefs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dependency wiring
        val localDataStore = LocalDataStore(dataStore)
        val userService = UserService(localDataStore)

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(userService, localDataStore) as T
            }
        }
        val authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        setContent {
            BeepPiscaTheme {
                var showLogin by remember { mutableStateOf(true) }
                val loginState by authViewModel.loginState.collectAsState()
                val registerState by authViewModel.registerState.collectAsState()
                val userInfo by authViewModel.userInfo.collectAsState()

                if (userInfo == null) {
                    if (showLogin) {
                        LoginScreenStateful(
                            viewModel = authViewModel,
                            onLoginSuccess = { /* do nothing, userInfo will auto update */ }
                        )
                        TextButton(onClick = { showLogin = false }) { Text("No account? Register") }
                    } else {
                        RegisterScreen(
                            registerState = registerState,
                            onRegister = { u, p, e, t -> authViewModel.register(u, p, e, t) },
                            onRegisterSuccess = { showLogin = true }
                        )
                        TextButton(onClick = { showLogin = true }) { Text("Have an account? Login") }
                    }
                } else {
                    LoggedInScreen(
                        user = userInfo!!,
                        onLogout = { authViewModel.logout() }
                    )
                }
            }
        }
    }
}
