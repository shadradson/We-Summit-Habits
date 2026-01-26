package com.humangamestats.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Utility object for triggering widget updates from the app.
 * Call [updateTodayWidget] whenever stat records are modified.
 */
object WidgetUpdater {
    
    /**
     * Update all instances of the Today widget.
     * Call this after saving, updating, or deleting records.
     */
    suspend fun updateTodayWidget(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val manager = GlanceAppWidgetManager(context)
                val widget = TodayWidget()
                val glanceIds = manager.getGlanceIds(TodayWidget::class.java)
                
                glanceIds.forEach { glanceId ->
                    widget.update(context, glanceId)
                }
            } catch (e: Exception) {
                // Widget might not be placed, ignore errors
                e.printStackTrace()
            }
        }
    }
}
