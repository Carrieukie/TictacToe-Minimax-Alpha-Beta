package com.example.minimaxtictactoe.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.models.ThemeWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    items: List<ThemeWrapper>,
    selectedItem: MutableState<ThemeWrapper>,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    setTheme: (Int) -> Unit
) {
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