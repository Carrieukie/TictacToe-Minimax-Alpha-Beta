package com.example.minimaxtictactoe.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.minimaxtictactoe.ui.theme.Theme

// To be used to set the preferred theme inside settings
sealed class ThemeWrapper(
    val themeName: String,
    val themeIcon: ImageVector,
    val themeValue: Theme
) {
    data class System(val name: String, val icon: ImageVector, val themeValu: Theme) : ThemeWrapper(
        themeName = name,
        themeIcon = icon,
        themeValue = themeValu
    )

    data class LightMode(val name: String, val icon: ImageVector, val themeValu: Theme) : ThemeWrapper(
        name, icon, themeValue = themeValu
    )

    data class DarkMode(val name: String, val icon: ImageVector, val themeValu: Theme) : ThemeWrapper(
        name, icon, themeValue = themeValu
    )

    data class MaterialYou(val name: String, val icon: ImageVector, val themeValu: Theme) : ThemeWrapper(
        name, icon, themeValue = themeValu
    )
}