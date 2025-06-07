package fct.nova.beeppisca.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeepPiscaTheme {
                SettingsScreen()
            }
        }
    }
}
