package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = MalagasyGold,
    onPrimary = Color.Black,
    primaryContainer = MalagasyGreenDark,
    onPrimaryContainer = MalagasyGoldLight,
    secondary = MalagasyGreenLight,
    onSecondary = Color.White,
    tertiary = MalagasyGoldLight,
    background = SurfaceDark,
    surface = CardDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = MalagasyGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8F3DC),
    onPrimaryContainer = MalagasyGreenDark,
    secondary = MalagasyGold,
    onSecondary = Color(0xFF1B4332),
    tertiary = MalagasyGoldDark,
    background = SurfaceLight,
    surface = CardLight,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderLight,
    outlineVariant = BorderLight
  )

@Composable
fun TsenaMalagasyTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Set false to maintain brand Green & Gold identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
