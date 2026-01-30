package com.humangamestats.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humangamestats.BuildConfig
import com.humangamestats.R
import com.humangamestats.data.datastore.SettingsDataStore
import com.humangamestats.data.datastore.ThemeMode
import com.humangamestats.ui.components.Watermark
import com.humangamestats.ui.theme.LocalAppColors

/**
 * Settings screen for app configuration.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onExportImportClick: () -> Unit,
    onTemplatesClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appColors = LocalAppColors.current
    
    Scaffold(

        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                appColors.gradientStart,
                                appColors.gradientEnd
                            )
                        )
                    )
            ) {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        //containerColor = MaterialTheme.colorScheme.primaryContainer,
                        //titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        //navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        navigationIconContentColor = Color.White
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Watermark in background
            Watermark()
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
            // Data Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Your Data",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        DataStat(
                            icon = Icons.Default.Category,
                            value = uiState.categoryCount.toString(),
                            label = "Categories",
                            modifier = Modifier.weight(1f)
                        )
                        DataStat(
                            icon = Icons.Default.ShowChart,
                            value = uiState.statCount.toString(),
                            label = "Stats",
                            modifier = Modifier.weight(1f)
                        )
                        DataStat(
                            icon = Icons.Default.DataObject,
                            value = uiState.recordCount.toString(),
                            label = "Records",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Location Settings
            SectionHeader(text = "Location")
            
            SwitchSetting(
                title = stringResource(R.string.auto_capture_location),
                subtitle = stringResource(R.string.auto_capture_location_desc),
                icon = Icons.Default.LocationOn,
                checked = uiState.autoCaptureLocation,
                onCheckedChange = { viewModel.setAutoCaptureLocation(it) }
            )
            
            HorizontalDivider()
            
            // Appearance Settings
            SectionHeader(text = "Appearance")
            
            var showThemeMenu by remember { mutableStateOf(false) }
            DropdownSetting(
                title = stringResource(R.string.theme),
                subtitle = uiState.themeMode.name.lowercase().replaceFirstChar { it.uppercase() },
                icon = Icons.Default.DarkMode,
                expanded = showThemeMenu,
                onExpandedChange = { showThemeMenu = it },
                onDismiss = { showThemeMenu = false }
            ) {
                ThemeMode.entries.forEach { mode ->
                    DropdownMenuItem(
                        text = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            viewModel.setThemeMode(mode)
                            showThemeMenu = false
                        }
                    )
                }
            }
            
            SwitchSetting(
                title = "Dynamic Colors",
                subtitle = "Use Material You colors (Android 12+)",
                icon = Icons.Default.Palette,
                checked = uiState.useDynamicColors,
                onCheckedChange = { viewModel.setUseDynamicColors(it) }
            )
            
            // Manual color selection (only shown when dynamic colors is OFF)
            AnimatedVisibility(visible = !uiState.useDynamicColors) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp) // Indent to align with other settings
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Custom Colors",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Gradient preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(uiState.gradientStartColor),
                                        Color(uiState.gradientEndColor)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Gradient Preview",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Left Gradient Color
                    ColorPickerSetting(
                        title = "Left Gradient Color",
                        selectedColor = uiState.gradientStartColor,
                        onColorSelected = { viewModel.setGradientStartColor(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Right Gradient Color
                    ColorPickerSetting(
                        title = "Right Gradient Color",
                        selectedColor = uiState.gradientEndColor,
                        onColorSelected = { viewModel.setGradientEndColor(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // New Item Button Color (FAB)
                    ColorPickerSetting(
                        title = "New Item Button Color",
                        selectedColor = uiState.iconAccentColor,
                        onColorSelected = { viewModel.setIconAccentColor(it) }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            HorizontalDivider()
            
            // Date Format
            var showDateFormatMenu by remember { mutableStateOf(false) }
            DropdownSetting(
                title = stringResource(R.string.date_format),
                subtitle = uiState.dateFormat,
                icon = null,
                expanded = showDateFormatMenu,
                onExpandedChange = { showDateFormatMenu = it },
                onDismiss = { showDateFormatMenu = false }
            ) {
                SettingsDataStore.DATE_FORMATS.forEach { format ->
                    DropdownMenuItem(
                        text = { Text(format) },
                        onClick = {
                            viewModel.setDateFormat(format)
                            showDateFormatMenu = false
                        }
                    )
                }
            }
            
            HorizontalDivider()
            
            // Data Management
            SectionHeader(text = "Data")
            
            ClickableSetting(
                title = "Data Point Templates",
                subtitle = "Manage reusable templates for stats",
                icon = Icons.Default.Layers,
                onClick = onTemplatesClick
            )
            
            ClickableSetting(
                title = stringResource(R.string.export_data) + " / " + stringResource(R.string.import_data),
                subtitle = "Export or import your data as JSON",
                icon = Icons.Default.DataObject,
                onClick = onExportImportClick
            )
            
            HorizontalDivider()
            
            // About
            SectionHeader(text = stringResource(R.string.about))
            
            ClickableSetting(
                title = stringResource(R.string.app_name),
                subtitle = "${stringResource(R.string.version)} ${BuildConfig.VERSION_NAME}",
                icon = Icons.Default.Info,
                onClick = { }
            )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SwitchSetting(
    title: String,
    subtitle: String,
    icon: ImageVector?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun DropdownSetting(
    title: String,
    subtitle: String,
    icon: ImageVector?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dropdownContent: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onExpandedChange(true) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismiss
            ) {
                dropdownContent()
            }
        }
    }
}

@Composable
private fun ClickableSetting(
    title: String,
    subtitle: String,
    icon: ImageVector?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DataStat(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ColorPickerSetting(
    title: String,
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Selected color circle
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(selectedColor))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
        )
    }
    
    if (showDialog) {
        ColorPickerDialog(
            initialColor = selectedColor,
            onDismiss = { showDialog = false },
            onColorSelected = {
                onColorSelected(it)
                showDialog = false
            }
        )
    }
}

/**
 * HSV Color Picker Dialog with Hue, Saturation, and Value sliders.
 */
@Composable
private fun ColorPickerDialog(
    initialColor: Long,
    onDismiss: () -> Unit,
    onColorSelected: (Long) -> Unit
) {
    // Convert initial color to HSV
    val initialComposeColor = Color(initialColor)
    val hsv = remember {
        val result = FloatArray(3)
        android.graphics.Color.RGBToHSV(
            (initialComposeColor.red * 255).toInt(),
            (initialComposeColor.green * 255).toInt(),
            (initialComposeColor.blue * 255).toInt(),
            result
        )
        result
    }
    
    var hue by remember { mutableFloatStateOf(hsv[0]) }
    var saturation by remember { mutableFloatStateOf(hsv[1]) }
    var value by remember { mutableFloatStateOf(hsv[2]) }
    
    // Calculate current color from HSV
    val currentColor = remember(hue, saturation, value) {
        Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value)))
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Color") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Color preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(currentColor)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
                
                // Hue slider (0-360)
                Column {
                    Text(
                        text = "Hue: ${hue.toInt()}°",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Hue gradient background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Red,
                                        Color.Yellow,
                                        Color.Green,
                                        Color.Cyan,
                                        Color.Blue,
                                        Color.Magenta,
                                        Color.Red
                                    )
                                )
                            )
                    )
                    Slider(
                        value = hue,
                        onValueChange = { hue = it },
                        valueRange = 0f..360f,
                        colors = SliderDefaults.colors(
                            thumbColor = currentColor,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        )
                    )
                }
                
                // Saturation slider (0-1)
                Column {
                    Text(
                        text = "Saturation: ${(saturation * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Saturation gradient (gray to full color)
                    val saturationStartColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, 0f, value)))
                    val saturationEndColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, 1f, value)))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(saturationStartColor, saturationEndColor)
                                )
                            )
                    )
                    Slider(
                        value = saturation,
                        onValueChange = { saturation = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = currentColor,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        )
                    )
                }
                
                // Value/Brightness slider (0-1)
                Column {
                    Text(
                        text = "Brightness: ${(value * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Value gradient (black to full color)
                    val valueStartColor = Color.Black
                    val valueEndColor = Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, 1f)))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(valueStartColor, valueEndColor)
                                )
                            )
                    )
                    Slider(
                        value = value,
                        onValueChange = { value = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = currentColor,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Convert current color to Long (ARGB format)
                    val argb = android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value))
                    // Convert to unsigned long with full alpha
                    val colorLong = (0xFF000000L or (argb.toLong() and 0xFFFFFFL))
                    onColorSelected(colorLong)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
