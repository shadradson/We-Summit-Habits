package com.humangamestats.ui.navigation

/**
 * Sealed class representing all navigation destinations in the app.
 * Uses type-safe navigation with Navigation Compose.
 */
sealed class Screen(val route: String) {
    /**
     * Home screen showing all categories.
     */
    data object Categories : Screen("categories")
    
    /**
     * Category detail screen showing all stats within a category.
     * @param categoryId ID of the category to display
     */
    data object CategoryDetail : Screen("category/{categoryId}") {
        fun createRoute(categoryId: Long) = "category/$categoryId"
        const val ARG_CATEGORY_ID = "categoryId"
    }
    
    /**
     * Stat detail screen showing all records for a stat with chart.
     * @param statId ID of the stat to display
     */
    data object StatDetail : Screen("stat/{statId}") {
        fun createRoute(statId: Long) = "stat/$statId"
        const val ARG_STAT_ID = "statId"
    }
    
    /**
     * Create/Edit stat screen.
     * @param categoryId ID of the parent category
     * @param statId Optional ID of the stat to edit (null for create)
     */
    data object StatForm : Screen("stat_form/{categoryId}?statId={statId}") {
        fun createRoute(categoryId: Long, statId: Long? = null): String {
            return if (statId != null) {
                "stat_form/$categoryId?statId=$statId"
            } else {
                "stat_form/$categoryId"
            }
        }
        const val ARG_CATEGORY_ID = "categoryId"
        const val ARG_STAT_ID = "statId"
    }
    
    /**
     * Create/Edit record screen.
     * @param statId ID of the parent stat
     * @param recordId Optional ID of the record to edit (null for create)
     */
    data object RecordForm : Screen("record_form/{statId}?recordId={recordId}") {
        fun createRoute(statId: Long, recordId: Long? = null): String {
            return if (recordId != null) {
                "record_form/$statId?recordId=$recordId"
            } else {
                "record_form/$statId"
            }
        }
        const val ARG_STAT_ID = "statId"
        const val ARG_RECORD_ID = "recordId"
    }
    
    /**
     * Settings screen for app preferences.
     */
    data object Settings : Screen("settings")
    
    /**
     * Export/Import screen for data management.
     */
    data object ExportImport : Screen("export_import")
    
    /**
     * Data Point Templates screen for managing reusable templates.
     */
    data object Templates : Screen("templates")
}
