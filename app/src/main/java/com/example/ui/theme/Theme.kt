package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MoneyHarvestDarkColorScheme =
  darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color(0xFF032215),
    primaryContainer = EmeraldCardSurface,
    onPrimaryContainer = EmeraldBright,
    secondary = HarvestGold,
    onSecondary = Color(0xFF1F1200),
    secondaryContainer = HarvestGoldDeep,
    onSecondaryContainer = HarvestGoldLight,
    tertiary = CyberCyan,
    onTertiary = Color(0xFF00262D),
    background = EmeraldDarkBg,
    onBackground = TextPrimaryDark,
    surface = EmeraldSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = EmeraldCardSurface,
    onSurfaceVariant = TextSecondaryDark,
    outline = EmeraldBorder,
    error = ScamAlertRed,
    onError = Color.White,
  )

private val MoneyHarvestLightColorScheme =
  lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = LightCard,
    onPrimaryContainer = TextPrimaryLight,
    secondary = Color(0xFFD97706),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = Color(0xFF0891B2),
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFA7F3D0),
    error = ScamAlertRed,
    onError = Color.White,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) MoneyHarvestDarkColorScheme else MoneyHarvestLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
