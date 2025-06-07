package fct.nova.beeppisca.ui.components

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fct.nova.beeppisca.R
import fct.nova.beeppisca.ui.validation.TicketValidationActivity

/**
 * A specialization of DefaultButton that launches the ticket‐validation flow.
 */
// app/src/main/java/fct/nova/beeppisca/ui/components/ValidateButton.kt
@Composable
fun ValidateButton(
    userId: String?,
    modifier: Modifier = Modifier
) {
    val ctx    = LocalContext.current
    // don’t show if we don’t have a user
    if (userId == null) return

    DefaultButton(
        text     = ctx.getString(R.string.button_validate_ticket),
        onClick  = {
            val intent = Intent(ctx, TicketValidationActivity::class.java)
                .putExtra("EXTRA_USER_ID", userId)
            ctx.startActivity(intent)
        },
        modifier = modifier
    )
}
