package com.humangamestats.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.humangamestats.model.DataPoint
import com.humangamestats.model.StatRecord
import com.humangamestats.model.StatType
import com.humangamestats.ui.theme.ChartGrid
import com.humangamestats.ui.theme.toChartColor
import java.util.Calendar

private const val MAX_SERIES = 4
private const val MAX_LABEL_POINTS = 15
private const val CHECKBOX_MAX_PER_WEEK = 7

/** Returns the Monday 00:00:00 of the week containing [timestamp]. */
private fun getWeekStart(timestamp: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timestamp
    val daysFromMonday = (cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7
    cal.add(Calendar.DAY_OF_YEAR, -daysFromMonday)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private data class ChartSeries(
    val dataPoint: DataPoint,
    val index: Int,
    val color: Color,
    val points: List<Pair<Long, Double>>,
    val minVal: Double,
    val maxVal: Double
)

/**
 * Multi-series line chart for displaying stat records over time.
 * Graphs up to 4 data points, each independently normalized to fit the same canvas.
 * Value labels are shown above each plotted point (when ≤15 points in the series).
 */
@Composable
fun StatChart(
    records: List<StatRecord>,
    dataPoints: List<DataPoint>,
    modifier: Modifier = Modifier
) {
    if (records.size < 2 || dataPoints.isEmpty()) return

    val seriesDataPoints = dataPoints.take(MAX_SERIES)
    val textMeasurer = rememberTextMeasurer()

    val allSeries = remember(records, dataPoints) {
        seriesDataPoints.mapIndexed { index, dp ->
            val rawData: List<Pair<Long, Double>> = if (dp.type == StatType.CHECKBOX) {
                // Aggregate "true" entries by calendar week → count per week (0–7)
                val weekCounts = mutableMapOf<Long, Int>()
                records.forEach { record ->
                    val rawValue = record.getValueForDataPoint(dp.id)
                        ?: record.values.getOrNull(index)?.value
                        ?: if (index == 0) @Suppress("DEPRECATION") record.value else null
                    if (rawValue == "true") {
                        val weekStart = getWeekStart(record.recordedAt)
                        weekCounts[weekStart] = (weekCounts[weekStart] ?: 0) + 1
                    }
                }
                weekCounts.entries
                    .sortedBy { it.key }
                    .map { (weekStart, count) ->
                        weekStart to minOf(count, CHECKBOX_MAX_PER_WEEK).toDouble()
                    }
            } else {
                records.mapNotNull { record ->
                    val rawValue = record.getValueForDataPoint(dp.id)
                        ?: record.values.getOrNull(index)?.value
                        ?: if (index == 0) @Suppress("DEPRECATION") record.value else null
                    val numVal = when (dp.type) {
                        StatType.NUMBER   -> rawValue?.toDoubleOrNull()
                        StatType.DURATION -> rawValue?.toLongOrNull()?.toDouble()
                        StatType.RATING   -> rawValue?.toIntOrNull()?.toDouble()
                        StatType.CHECKBOX -> null // handled above
                    }
                    numVal?.let { record.recordedAt to it }
                }.sortedBy { it.first }
            }

            val color = dp.color.toChartColor(index)
            // Checkbox series always uses a fixed 0–7 scale
            val minVal = if (dp.type == StatType.CHECKBOX) 0.0 else rawData.minOfOrNull { it.second } ?: 0.0
            val maxVal = if (dp.type == StatType.CHECKBOX) CHECKBOX_MAX_PER_WEEK.toDouble() else rawData.maxOfOrNull { it.second } ?: 1.0

            ChartSeries(dp, index, color, rawData, minVal, maxVal)
        }.filter { it.points.size >= 2 }
    }

    if (allSeries.isEmpty()) return

    val minTime = allSeries.minOf { s -> s.points.first().first }
    val maxTime = allSeries.maxOf { s -> s.points.last().first }
    val timeRange = if (maxTime == minTime) 1L else maxTime - minTime

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
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
        ) {
            val width = size.width
            val height = size.height

            // Horizontal grid lines
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

            // Draw each series
            allSeries.forEach { series ->
                val valRange = if (series.maxVal == series.minVal) 1.0
                               else series.maxVal - series.minVal

                val points = series.points.map { (time, value) ->
                    val x = ((time - minTime).toFloat() / timeRange) * width
                    val normalizedY = (value - series.minVal) / valRange
                    val y = height - (normalizedY.toFloat() * height)
                    Offset(x, y)
                }

                // Fill area under line
                val fillPath = Path().apply {
                    moveTo(points.first().x, height)
                    points.forEach { lineTo(it.x, it.y) }
                    lineTo(points.last().x, height)
                    close()
                }
                drawPath(path = fillPath, color = series.color.copy(alpha = 0.12f))

                // Line segments
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = series.color,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Point circles
                points.forEach { point ->
                    drawCircle(color = series.color, radius = 4.dp.toPx(), center = point)
                    drawCircle(color = Color.White, radius = 2.dp.toPx(), center = point)
                }

                // Value labels — only when series is not too dense
                if (series.points.size <= MAX_LABEL_POINTS) {
                    val labelStyle = TextStyle(
                        color = series.color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    series.points.forEachIndexed { i, (_, value) ->
                        val label = formatChartLabel(value, series.dataPoint)
                        val textLayout = textMeasurer.measure(label, labelStyle)
                        val labelX = (points[i].x - textLayout.size.width / 2f)
                            .coerceIn(0f, width - textLayout.size.width)
                        val labelY = (points[i].y - textLayout.size.height - 4.dp.toPx())
                            .coerceAtLeast(0f)
                        drawText(
                            textLayoutResult = textLayout,
                            topLeft = Offset(labelX, labelY)
                        )
                    }
                }
            }
        }

        // Legend row (only when multiple series are drawn)
        if (allSeries.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                allSeries.forEachIndexed { i, series ->
                    if (i > 0) Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(series.color)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = series.dataPoint.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = series.color
                    )
                }
            }
        }
    }
}

private fun formatChartLabel(value: Double, dataPoint: DataPoint): String {
    return when (dataPoint.type) {
        StatType.NUMBER -> {
            val formatted = if (value == value.toLong().toDouble()) {
                value.toLong().toString()
            } else {
                String.format("%.1f", value)
            }
            if (dataPoint.unit.isNotEmpty()) "$formatted ${dataPoint.unit}" else formatted
        }
        StatType.DURATION -> {
            val seconds = value.toLong()
            when {
                seconds >= 3600 -> "${seconds / 3600}h ${(seconds % 3600) / 60}m"
                seconds >= 60   -> "${seconds / 60}m ${seconds % 60}s"
                else            -> "${seconds}s"
            }
        }
        StatType.RATING   -> "${value.toInt()}★"
        StatType.CHECKBOX -> "${value.toInt()}/$CHECKBOX_MAX_PER_WEEK"
    }
}
