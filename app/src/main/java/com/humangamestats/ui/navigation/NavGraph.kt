package com.humangamestats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.humangamestats.ui.screens.categories.CategoriesScreen
import com.humangamestats.ui.screens.category_detail.CategoryDetailScreen
import com.humangamestats.ui.screens.record.RecordFormScreen
import com.humangamestats.ui.screens.settings.ExportImportScreen
import com.humangamestats.ui.screens.settings.SettingsScreen
import com.humangamestats.ui.screens.stat.StatDetailScreen
import com.humangamestats.ui.screens.stat.StatFormScreen
import com.humangamestats.ui.screens.templates.TemplatesScreen

/**
 * Main navigation graph for the app.
 * Defines all navigation destinations and their arguments.
 */
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Categories.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Categories list (home screen) with embedded Today tab
        composable(route = Screen.Categories.route) {
            CategoriesScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.CategoryDetail.createRoute(categoryId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onStatClick = { statId ->
                    navController.navigate(Screen.StatDetail.createRoute(statId))
                }
            )
        }
        
        // Category detail (stats list)
        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(
                navArgument(Screen.CategoryDetail.ARG_CATEGORY_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong(Screen.CategoryDetail.ARG_CATEGORY_ID) ?: 0L
            CategoryDetailScreen(
                categoryId = categoryId,
                onBackClick = { navController.popBackStack() },
                onStatClick = { statId ->
                    navController.navigate(Screen.StatDetail.createRoute(statId))
                },
                onAddStatClick = {
                    navController.navigate(Screen.StatForm.createRoute(categoryId))
                }
            )
        }
        
        // Stat detail (records list with chart)
        composable(
            route = Screen.StatDetail.route,
            arguments = listOf(
                navArgument(Screen.StatDetail.ARG_STAT_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val statId = backStackEntry.arguments?.getLong(Screen.StatDetail.ARG_STAT_ID) ?: 0L
            StatDetailScreen(
                statId = statId,
                onBackClick = { navController.popBackStack() },
                onAddRecordClick = {
                    navController.navigate(Screen.RecordForm.createRoute(statId))
                },
                onEditRecordClick = { recordId ->
                    navController.navigate(Screen.RecordForm.createRoute(statId, recordId))
                },
                onEditStatClick = { categoryId ->
                    navController.navigate(Screen.StatForm.createRoute(categoryId, statId))
                }
            )
        }
        
        // Stat form (create/edit)
        composable(
            route = Screen.StatForm.route,
            arguments = listOf(
                navArgument(Screen.StatForm.ARG_CATEGORY_ID) {
                    type = NavType.LongType
                },
                navArgument(Screen.StatForm.ARG_STAT_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong(Screen.StatForm.ARG_CATEGORY_ID) ?: 0L
            val statId = backStackEntry.arguments?.getLong(Screen.StatForm.ARG_STAT_ID) ?: -1L
            StatFormScreen(
                categoryId = categoryId,
                statId = if (statId == -1L) null else statId,
                onBackClick = { navController.popBackStack() },
                onSaveComplete = { navController.popBackStack() },
                onDeleteComplete = {
                    // Navigate back to category detail, popping both form and stat detail
                    navController.popBackStack(Screen.CategoryDetail.createRoute(categoryId), inclusive = false)
                }
            )
        }
        
        // Record form (create/edit)
        composable(
            route = Screen.RecordForm.route,
            arguments = listOf(
                navArgument(Screen.RecordForm.ARG_STAT_ID) {
                    type = NavType.LongType
                },
                navArgument(Screen.RecordForm.ARG_RECORD_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val statId = backStackEntry.arguments?.getLong(Screen.RecordForm.ARG_STAT_ID) ?: 0L
            val recordId = backStackEntry.arguments?.getLong(Screen.RecordForm.ARG_RECORD_ID) ?: -1L
            RecordFormScreen(
                statId = statId,
                recordId = if (recordId == -1L) null else recordId,
                onBackClick = { navController.popBackStack() },
                onSaveComplete = { navController.popBackStack() }
            )
        }
        
        // Settings
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onExportImportClick = {
                    navController.navigate(Screen.ExportImport.route)
                },
                onTemplatesClick = {
                    navController.navigate(Screen.Templates.route)
                }
            )
        }
        
        // Export/Import
        composable(route = Screen.ExportImport.route) {
            ExportImportScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Data Point Templates
        composable(route = Screen.Templates.route) {
            TemplatesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
