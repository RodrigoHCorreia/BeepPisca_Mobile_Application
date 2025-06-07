package fct.nova.beeppisca.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import fct.nova.beeppisca.domain.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LocalDataStore(private val dataStore: DataStore<Preferences>) {

    private val _userData = MutableStateFlow<Map<String, String>>(emptyMap())
    val userData: Flow<Map<String, String>> get() = _userData

    fun saveUserPreference(key: String, value: String) {
        // Simulate saving user preferences to DataStore
        _userData.value = _userData.value + (key to value)
    }

    fun getUserPreference(key: String): String? {
        return _userData.value[key]
    }

    fun getUserTickets(): Flow<List<Ticket>> {
        // Simulating fetching user tickets from local storage (mock data for now)
        return MutableStateFlow(emptyList())
    }

    fun saveUserTicket(ticket: Ticket) {
        // Simulate saving a ticket to local storage
    }
}
