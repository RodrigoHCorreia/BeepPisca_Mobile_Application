package fct.nova.beeppisca.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.ui.home.NoStopActivity
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class SearchActivity : ComponentActivity() {
    private val vm: SearchViewModel by viewModels()

    companion object {
        private const val EXTRA_LOC = "extra_loc"
        private const val EXTRA_USER = "extra_user"
        fun launch(ctx: Context, loc: MomentLocation, userId: String?) {
            ctx.startActivity(Intent(ctx, SearchActivity::class.java).apply {
                putExtra(EXTRA_LOC, loc)
                putExtra(EXTRA_USER, userId)
            })
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loc    = intent.getParcelableExtra<MomentLocation>(EXTRA_LOC)!!
        val userId = intent.getStringExtra(EXTRA_USER)

        setContent {
            BeepPiscaTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            navigationIcon = {
                                IconButton(onClick = {
                                    // back to NoStop
                                    NoStopActivity.launch(this, loc, userId)
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.back),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            title = {
                                Image(
                                    painter = painterResource(R.drawable.beeppisca),
                                    contentDescription = stringResource(R.string.app_name),
                                    modifier = Modifier.height(48.dp)
                                )
                            },
                            actions = {
                                Image(
                                    painter = painterResource(R.drawable.carris_logo),
                                    contentDescription = stringResource(R.string.carris_logo_desc),
                                    modifier = Modifier.height(36.dp)
                                )
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    // pass through the padding
                    SearchScreen(
                        viewModel = vm,
                        userLoc   = loc,
                        modifier = Modifier.padding(innerPadding),
                        onSelect  = { result ->
                            // TODO: navigate to details or map, for now just noop
                        }
                    )
                }
            }
        }
    }
}
