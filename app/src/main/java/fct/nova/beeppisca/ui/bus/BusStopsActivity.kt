package fct.nova.beeppisca.ui.bus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class BusStopsActivity : ComponentActivity() {

    private val busViewModel: BusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeepPiscaTheme {
                BusScreen(viewModel = busViewModel)
            }
        }
    }
}
