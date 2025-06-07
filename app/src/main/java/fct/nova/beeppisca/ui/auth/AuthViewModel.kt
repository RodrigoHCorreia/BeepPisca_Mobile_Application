package fct.nova.beeppisca.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.SimpleUser
import fct.nova.beeppisca.service.user.UserService
import fct.nova.beeppisca.storage.LocalDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val api: UserService,
    private val localDataStore: LocalDataStore
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<LoginState>(LoginState.Idle)
    val registerState: StateFlow<LoginState> = _registerState

    // This is always synced with the cache
    private val _userInfo = MutableStateFlow<SimpleUser?>(null)
    val userInfo: StateFlow<SimpleUser?> = _userInfo

    init {
        // Listen to cached user info and always reflect latest value
        viewModelScope.launch {
            localDataStore.cachedSimpleUser.collect { user ->
                _userInfo.value = user
            }
        }
    }

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val ok = api.login(username, password)
            if (ok) {
                val user = api.getMe()
                user?.let { localDataStore.saveSimpleUser(it) }
                _loginState.value = if (user != null) LoginState.Success else LoginState.Error("Failed to fetch user info")
            } else {
                _loginState.value = LoginState.Error("Invalid credentials")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            api.logout()
            localDataStore.clearSimpleUser()
            _loginState.value = LoginState.Idle
        }
    }

    fun register(username: String, password: String, email: String, type: String) {
        _registerState.value = LoginState.Loading
        viewModelScope.launch {
            val ok = api.register(username, password, email, type)
            _registerState.value = if (ok) LoginState.Success else LoginState.Error("Register failed")
        }
    }

    fun resetState() { _loginState.value = LoginState.Idle }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val msg: String) : LoginState()
}
