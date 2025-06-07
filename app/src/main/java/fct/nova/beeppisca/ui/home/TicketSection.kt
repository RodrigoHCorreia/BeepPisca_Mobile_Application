package fct.nova.beeppisca.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fct.nova.beeppisca.ui.components.ValidateButton
import fct.nova.beeppisca.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import fct.nova.beeppisca.ui.theme.CarrisBlack

@Composable
fun TicketSection(
    imageRes: Int,
    infoText: String,
    userId: String?
) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(R.string.content_desc_ticket),
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = infoText,
            style = MaterialTheme.typography.bodyMedium,
            color = CarrisBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(24.dp))
        ValidateButton(userId = userId, modifier = Modifier.fillMaxWidth(0.8f))
    }
}
