package com.humangamestats.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.humangamestats.ui.theme.presetDataPointColors

/**
 * A row of color swatches for selecting a data point chart color.
 * The first swatch (Auto) uses null to indicate the default cycling palette.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DataPointColorPicker(
    selectedColor: String?,
    onColorSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        presetDataPointColors.forEach { colorHex ->
            ColorSwatch(
                colorHex = colorHex,
                isSelected = colorHex == selectedColor,
                onClick = { onColorSelected(colorHex) }
            )
        }
    }
}

@Composable
private fun ColorSwatch(
    colorHex: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val swatchColor = if (colorHex == null) {
        MaterialTheme.colorScheme.surface
    } else {
        try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            MaterialTheme.colorScheme.surface
        }
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(swatchColor)
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (colorHex == null) {
            Text(
                text = "A",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
    }
}
