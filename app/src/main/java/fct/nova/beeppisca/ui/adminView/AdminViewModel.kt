package fct.nova.beeppisca.ui.adminView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fct.nova.beeppisca.domain.Bus
import fct.nova.beeppisca.storage.BusRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repo: BusRepository = BusRepository()
) : ViewModel() {

    // 1) Backing state
    private val _buses = MutableStateFlow<List<Bus>>(emptyList())
    val buses: StateFlow<List<Bus>> = _buses.asStateFlow()

    // 2) Loading indicator
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true
            val all = repo.getAllBuses()
                .sortedByDescending { it.illegalOccupancy }
            _buses.value = all
            _loading.value = false
        }
    }
}
