package fct.nova.beeppisca.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LoginScreenStateful(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    LoginScreen(
        loginState = loginState,
        onLogin = viewModel::login,
        onLoginSuccess = onLoginSuccess
    )
}
