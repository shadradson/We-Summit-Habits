package com.humangamestats.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.ui.theme.ChartFill
import com.humangamestats.ui.theme.ChartGrid
import com.humangamestats.ui.theme.ChartLine

/**
 * Simple line chart component for displaying stat records over time.
 */
@Composable
fun StatChart(
    records: List<StatRecord>,
    statType: StatType,
    modifier: Modifier = Modifier
) {
    if (records.size < 2) return
    
    val chartData = remember(records, statType) {
        records.mapNotNull { record ->
            val value = when (statType) {
                StatType.NUMBER -> record.value.toDoubleOrNull()
                StatType.DURATION -> record.value.toLongOrNull()?.toDouble()
                StatType.RATING -> record.value.toIntOrNull()?.toDouble()
                StatType.CHECKBOX -> if (record.value == "true") 1.0 else 0.0
            }
            value?.let { record.recordedAt to it }
        }.sortedBy { it.first }
    }
    
    if (chartData.size < 2) return
    
    val lineColor = ChartLine
    val fillColor = ChartFill
    val gridColor = ChartGrid
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val width = size.width
            val height = size.height
            
            val minValue = chartData.minOf { it.second }
            val maxValue = chartData.maxOf { it.second }
            val valueRange = if (maxValue == minValue) 1.0 else maxValue - minValue
            
            val minTime = chartData.first().first
            val maxTime = chartData.last().first
            val timeRange = if (maxTime == minTime) 1L else maxTime - minTime
            
            // Draw grid lines
            val gridLines = 4
            for (i in 0..gridLines) {
                val y = height * i / gridLines
                drawLine(
                    color = gridColor.copy(alpha = 0.3f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }
            
            // Calculate points
            val points = chartData.map { (time, value) ->
                val x = ((time - minTime).toFloat() / timeRange) * width
                val y = height - ((value - minValue).toFloat() / valueRange.toFloat()) * height
                Offset(x, y)
            }
            
            // Draw fill area
            if (points.isNotEmpty()) {
                val fillPath = Path().apply {
                    moveTo(points.first().x, height)
                    points.forEach { point ->
                        lineTo(point.x, point.y)
                    }
                    lineTo(points.last().x, height)
                    close()
                }
                drawPath(
                    path = fillPath,
                    color = fillColor
                )
            }
            
            // Draw line
            if (points.size >= 2) {
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = lineColor,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
            
            // Draw points
            points.forEach { point ->
                drawCircle(
                    color = lineColor,
                    radius = 4.dp.toPx(),
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.dp.toPx(),
                    center = point
                )
            }
        }
    }
}
