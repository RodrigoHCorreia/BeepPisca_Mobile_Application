package fct.nova.beeppisca

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import fct.nova.beeppisca.storage.LocalDataStore

class BeepPiscaApp : Application() {

    val dataStore by preferencesDataStore(name = "user_info")
    val ticketRepository by lazy { LocalDataStore(dataStore) }

    override fun onCreate() {
        super.onCreate()
    }
}
