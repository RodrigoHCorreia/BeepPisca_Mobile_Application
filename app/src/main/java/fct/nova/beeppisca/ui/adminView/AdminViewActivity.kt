package fct.nova.beeppisca.ui.adminView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class AdminViewActivity : ComponentActivity() {
    private val vm: AdminViewModel by viewModels()

    companion object {
        private const val EXTRA_USER_NAME = "extra_user_name"
        fun launch(ctx: Context, userName: String) {
            ctx.startActivity(Intent(ctx, AdminViewActivity::class.java).apply {
                putExtra(EXTRA_USER_NAME, userName)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra(EXTRA_USER_NAME) ?: "Admin"

        setContent {
            BeepPiscaTheme {
                AdminViewScreen(
                    username = name,
                    buses = vm.buses.collectAsState().value,
                    loading = vm.loading.collectAsState().value,
                )
            }
        }
    }
}
