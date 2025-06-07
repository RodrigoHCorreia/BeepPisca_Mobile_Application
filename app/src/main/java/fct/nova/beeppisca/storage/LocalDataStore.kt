package fct.nova.beeppisca.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import fct.nova.beeppisca.domain.SimpleUser
import fct.nova.beeppisca.domain.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class LocalDataStore(private val dataStore: DataStore<Preferences>) {
    private val gson = Gson()
    private val SIMPLE_USER_KEY = stringPreferencesKey("simple_user_json")
    private val SESSION_COOKIE_KEY = stringPreferencesKey("session_cookie")

    /**
     * Save the whole SimpleUser object as JSON.
     */
    suspend fun saveSimpleUser(user: SimpleUser) {
        val json = gson.toJson(user)
        dataStore.edit { it[SIMPLE_USER_KEY] = json }
    }

    /**
     * Get the cached SimpleUser as a Flow (null if not cached)
     */
    val cachedSimpleUser: Flow<SimpleUser?> = dataStore.data
        .map { prefs ->
            prefs[SIMPLE_USER_KEY]?.let { gson.fromJson(it, SimpleUser::class.java) }
        }

    /** Save/load/clear session cookie **/
    suspend fun saveSessionCookie(cookie: String) {
        dataStore.edit { it[SESSION_COOKIE_KEY] = cookie }
    }

    val sessionCookieFlow: Flow<String?> = dataStore.data.map { it[SESSION_COOKIE_KEY] }

    suspend fun clearSessionCookie() {
        dataStore.edit { it.remove(SESSION_COOKIE_KEY) }
    }

    /**
     * Clear the cached user (e.g., on logout)
     */
    suspend fun clearSimpleUser() {
        dataStore.edit { it.remove(SIMPLE_USER_KEY) }
    }

    // ---------- Your existing mock logic for preferences and tickets (optional) ----------
    private val _userData = MutableStateFlow<Map<String, String>>(emptyMap())
    val userData: Flow<Map<String, String>> get() = _userData

    fun saveUserPreference(key: String, value: String) {
        _userData.value = _userData.value + (key to value)
    }

    fun getUserPreference(key: String): String? {
        return _userData.value[key]
    }

    fun getUserTickets(): Flow<List<Ticket>> {
        return MutableStateFlow(emptyList())
    }

    fun saveUserTicket(ticket: Ticket) {
        // No-op or implement as needed
    }
}
