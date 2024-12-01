package com.example.minimaxtictactoe.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.ui.mainscreencontent.model.ThemeWrapper
import com.example.minimaxtictactoe.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    setTheme: (Int) -> Unit
) {
    val items = listOf(
        ThemeWrapper.System("Use system theme", icon = Icons.Default.Settings, themeValu = Theme.FOLLOW_SYSTEM),
        ThemeWrapper.LightMode("Light Mode", icon = Icons.Default.ModeNight, themeValu = Theme.LIGHT_THEME),
        ThemeWrapper.DarkMode("Dark Mode", icon = Icons.Default.LightMode, themeValu = Theme.DARK_THEME),
        ThemeWrapper.MaterialYou("Material You", icon = Icons.Default.Mode, themeValu = Theme.MATERIAL_YOU),
    )
    val coroutineScope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(items[0]) }
    ModalDrawerSheet(
        Modifier
            .wrapContentSize()
    ) {
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = rememberVectorPainter(image = item.themeIcon),
                        contentDescription = null
                    )
                },
                label = { Text(item.themeName) },
                selected = item == selectedItem.value,
                onClick = {
                    coroutineScope.launch { drawerState.close() }
                    selectedItem.value = item
                    setTheme(item.themeValue.themeValue)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}