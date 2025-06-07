package fct.nova.beeppisca.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegisterScreen(
    registerState: LoginState,
    onRegister: (String, String, String, String) -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("regular") }

    Column(Modifier.padding(16.dp)) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type (e.g. regular/admin)") })
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onRegister(username, password, email, type) },
            enabled = registerState != LoginState.Loading
        ) {
            if (registerState == LoginState.Loading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Register")
        }
        if (registerState is LoginState.Error) {
            Spacer(Modifier.height(8.dp))
            Text(registerState.msg, color = MaterialTheme.colorScheme.error)
        }
        if (registerState == LoginState.Success) {
            LaunchedEffect(Unit) { onRegisterSuccess() }
        }
    }
}

@Preview(showBackground = true, name = "Register - Idle")
@Composable
fun RegisterScreenPreviewIdle() {
    RegisterScreen(
        registerState = LoginState.Idle,
        onRegister = { _, _, _, _ -> },
        onRegisterSuccess = {}
    )
}

@Preview(showBackground = true, name = "Register - Loading")
@Composable
fun RegisterScreenPreviewLoading() {
    RegisterScreen(
        registerState = LoginState.Loading,
        onRegister = { _, _, _, _ -> },
        onRegisterSuccess = {}
    )
}

@Preview(showBackground = true, name = "Register - Error")
@Composable
fun RegisterScreenPreviewError() {
    RegisterScreen(
        registerState = LoginState.Error("This username is already taken!"),
        onRegister = { _, _, _, _ -> },
        onRegisterSuccess = {}
    )
}

@Preview(showBackground = true, name = "Register - Success")
@Composable
fun RegisterScreenPreviewSuccess() {
    RegisterScreen(
        registerState = LoginState.Success,
        onRegister = { _, _, _, _ -> },
        onRegisterSuccess = {}
    )
}
