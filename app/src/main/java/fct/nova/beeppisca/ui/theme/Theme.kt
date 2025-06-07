package fct.nova.beeppisca.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

internal val LightColorScheme = lightColorScheme(
    primary            = CarrisBlue,
    onPrimary          = Color.White,
    secondary          = CarrisYellow,
    onSecondary        = CarrisBlack,
    background         = CarrisGrayBg,
    surface            = Color.White,
    onSurface          = CarrisBlack,
    surfaceVariant     = Color(0xFFE0E0E0),
    onSurfaceVariant   = CarrisBlack
)

@Composable
fun BeepPiscaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = AppTypography,
        content     = content
    )
}