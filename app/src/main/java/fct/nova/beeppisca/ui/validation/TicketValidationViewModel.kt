// app/src/main/java/fct/nova/beeppisca/ui/validation/TicketValidationViewModel.kt
package fct.nova.beeppisca.ui.validation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI states for validation.
 */
sealed class ValidationState {
    object Scanning    : ValidationState() // camera + looking
    object Validating  : ValidationState() // network in progress
    object Success     : ValidationState() // show green arrow
    object Error       : ValidationState() // show red cross
}

open class TicketValidationViewModel : ViewModel() {
    protected val _state = MutableStateFlow<ValidationState>(ValidationState.Scanning)
    open val state: StateFlow<ValidationState> = _state.asStateFlow()

    /** Call when QR code payload detected. */
    fun onQrDetected(qrValue: String, userId: String) {
        viewModelScope.launch {
            _state.value = ValidationState.Validating
            // simulate network delay
            delay(1500)
            // mock success or failure
            if (qrValue.hashCode() % 2 == 0) {
                _state.value = ValidationState.Success
            } else {
                _state.value = ValidationState.Error
            }
        }
    }

    /** Reset to scanning again (on “Try Again”). */
    fun reset() {
        _state.value = ValidationState.Scanning
    }
}
