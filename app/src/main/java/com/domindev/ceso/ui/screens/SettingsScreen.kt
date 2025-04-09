package com.domindev.ceso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.domindev.ceso.ui.components.CesoNavigationDrawer
import com.domindev.ceso.ui.event.Events
import com.domindev.ceso.ui.state.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: State,
    navController: NavHostController,
    onEvent: (Events) -> Unit,
    navigateBack: () -> Unit
) {
    var darkMode by remember { mutableStateOf(false) }
    var fontSize by remember { mutableIntStateOf(16) }
    var autoSave by remember { mutableStateOf(true) }
    var backupEnabled by remember { mutableStateOf(true) }
    var syncEnabled by remember { mutableStateOf(false) }

    CesoNavigationDrawer(state,navController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigateBack() // TODO
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Appearance Section
                SettingsSection(title = "Appearance") {
                    SettingsSwitchItem(
                        icon = Icons.Default.Favorite,
                        //icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                    SettingsSliderItem(
                        icon = Icons.Default.Create,
                        //icon = Icons.Default.TextFields,
                        title = "Font Size",
                        value = fontSize.toFloat(),
                        onValueChange = { fontSize = it.toInt() },
                        valueRange = 12f..24f,
                        valueText = "${fontSize}sp"
                    )
                }

                // Note Settings Section
                SettingsSection(title = "Note Settings") {
                    SettingsSwitchItem(
                        icon = Icons.AutoMirrored.Default.Send,
                        // icon = Icons.Default.Save,
                        title = "Auto Save",
                        checked = autoSave,
                        onCheckedChange = { autoSave = it }
                    )
                    SettingsSwitchItem(
                        icon = Icons.Default.MailOutline,
                        // icon = Icons.Default.Backup,
                        title = "Backup Notes",
                        checked = backupEnabled,
                        onCheckedChange = { backupEnabled = it }
                    )
                }

                // Sync Section
                SettingsSection(title = "Sync") {
                    SettingsSwitchItem(
                        icon = Icons.Default.Build,
                        // icon = Icons.Default.CloudSync,
                        title = "Cloud Sync",
                        checked = syncEnabled,
                        onCheckedChange = { syncEnabled = it }
                    )
                    if (syncEnabled) {
                        SettingsItem(
                            icon = Icons.Default.AccountCircle,
                            title = "Sync Account",
                            subtitle = "Not signed in",
                            onClick = { /* Handle sign in */ }
                        )
                    }
                }

                // About Section
                SettingsSection(title = "About") {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Version",
                        subtitle = "1.0.0"
                    )
                    SettingsItem(
                        icon = Icons.Default.Lock,
                        // icon = Icons.Default.PrivacyTip,
                        title = "Privacy Policy",
                        onClick = { /* Handle privacy policy */ }
                    )
                    SettingsItem(
                        icon = Icons.Default.ThumbUp,
                        // icon = Icons.Default.Description,
                        title = "Terms of Service",
                        onClick = { /* Handle terms of service */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = { onClick?.invoke() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    // imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun SettingsSliderItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    valueText: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
} 