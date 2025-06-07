package fct.nova.beeppisca.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import fct.nova.beeppisca.domain.SimpleUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataStore(private val dataStore: DataStore<Preferences>) {
    private val gson = Gson()

    // Key under which we store the current user JSON
    private val SIMPLE_USER_KEY    = stringPreferencesKey("simple_user_json")
    // Key under which we store the session cookie
    private val SESSION_COOKIE_KEY = stringPreferencesKey("session_cookie")

    companion object {
        /** Two predefined users */
        val ADMIN_USER = SimpleUser(
            id       = "admin01",
            name     = "Admin",
            email    = "admin@example.com",
            password = null,
            token    = null,
            isAdmin  = true
        )

        val BASIC_USER = SimpleUser(
            id       = "user01",
            name     = "Regular User",
            email    = "user@example.com",
            password = null,
            token    = null,
            isAdmin  = false
        )
    }

    /** Persist a SimpleUser to DataStore (i.e. “log in”). */
    suspend fun saveSimpleUser(user: SimpleUser) {
        val json = gson.toJson(user)
        dataStore.edit { prefs ->
            prefs[SIMPLE_USER_KEY] = json
        }
    }

    /** Save session cookie */
    suspend fun saveSessionCookie(cookie: String) {
        dataStore.edit { prefs ->
            prefs[SESSION_COOKIE_KEY] = cookie
        }
    }

    /** Clear session cookie */
    suspend fun clearSessionCookie() {
        dataStore.edit { prefs ->
            prefs.remove(SESSION_COOKIE_KEY)
        }
    }

    /** Flow of the stored session cookie (or null) */
    val sessionCookieFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[SESSION_COOKIE_KEY] }

    /** Convenience: log in as the built-in admin user */
    suspend fun loginAsAdmin() = saveSimpleUser(ADMIN_USER)

    /** Convenience: log in as the built-in basic user */
    suspend fun loginAsBasic() = saveSimpleUser(BASIC_USER)

    /** “Log out” by clearing the saved user */
    suspend fun clearSimpleUser() {
        dataStore.edit { prefs ->
            prefs.remove(SIMPLE_USER_KEY)
        }
    }

    /** Flow of the current cached user (or null if none) */
    val currentUser: Flow<SimpleUser?> = dataStore.data
        .map { prefs ->
            prefs[SIMPLE_USER_KEY]?.let {
                gson.fromJson(it, SimpleUser::class.java)
            }
        }

    /** Flow of just the userId (or null) */
    val userIdFlow: Flow<String?> = currentUser.map { it?.id }

    /** Flow of just the admin-flag (false if no user or non-admin) */
    val isAdminFlow: Flow<Boolean> = currentUser.map { it?.isAdmin == true }
}
