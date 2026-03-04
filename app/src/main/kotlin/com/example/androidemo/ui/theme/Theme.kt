package com.example.androidemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AndroidDemoTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useDarkTheme -> darkColorScheme(
            primary = primaryDark,
            secondary = secondaryDark,
            tertiary = tertiaryDark,
            error = errorDark
        )
        else -> lightColorScheme(
            primary = primaryLight,
            secondary = secondaryLight,
            tertiary = tertiaryLight,
            error = errorLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
