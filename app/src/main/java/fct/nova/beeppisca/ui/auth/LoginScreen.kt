package fct.nova.beeppisca.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Composable
fun LoginScreen(
    loginState: LoginState,
    onLogin: (String, String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onLogin(username, password) },
            enabled = loginState != LoginState.Loading
        ) {
            if (loginState == LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Login")
            }
        }

        if (loginState is LoginState.Error) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = (loginState).msg,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (loginState == LoginState.Success) {
            LaunchedEffect(Unit) {
                onLoginSuccess()
            }
        }
    }
}

@Preview(showBackground = true, name = "Idle State")
@Composable
fun LoginScreenPreviewIdle() {
    LoginScreen(
        loginState = LoginState.Idle,
        onLogin = { _, _ -> },
        onLoginSuccess = {}
    )
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun LoginScreenPreviewLoading() {
    LoginScreen(
        loginState = LoginState.Loading,
        onLogin = { _, _ -> },
        onLoginSuccess = {}
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun LoginScreenPreviewError() {
    LoginScreen(
        loginState = LoginState.Error("Invalid credentials"),
        onLogin = { _, _ -> },
        onLoginSuccess = {}
    )
}
