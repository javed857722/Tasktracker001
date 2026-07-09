package com.example.tasktracker001

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasktracker001.data.ActivityLogRepository
import com.example.tasktracker001.data.ProjectRepository
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.data.TaskDatabase
import com.example.tasktracker001.data.TaskRepository
import com.example.tasktracker001.data.UserRepository
import com.example.tasktracker001.ui.AdminLoginScreen
import com.example.tasktracker001.ui.AdminPanelScreen
import com.example.tasktracker001.ui.AdminSignUpScreen
import com.example.tasktracker001.ui.AdminTaskOversightScreen
import com.example.tasktracker001.ui.AdminUserManagementScreen
import com.example.tasktracker001.ui.AnalyticsScreen
import com.example.tasktracker001.ui.CalendarScreen
import com.example.tasktracker001.ui.CreateTaskScreen
import com.example.tasktracker001.ui.DashboardScreen
import com.example.tasktracker001.ui.EditUserScreen
import com.example.tasktracker001.ui.ForgotPasswordScreen
import com.example.tasktracker001.ui.LoginScreen
import com.example.tasktracker001.ui.ProfileScreen
import com.example.tasktracker001.ui.ProjectDashboardScreen
import com.example.tasktracker001.ui.ProjectDetailScreen
import com.example.tasktracker001.ui.ProjectViewModel
import com.example.tasktracker001.ui.ProjectViewModelFactory
import com.example.tasktracker001.ui.SettingsScreen
import com.example.tasktracker001.ui.SignUpScreen
import com.example.tasktracker001.ui.TaskDetailScreen
import com.example.tasktracker001.ui.TaskViewModel
import com.example.tasktracker001.ui.TaskViewModelFactory
import com.example.tasktracker001.ui.UserViewModel
import com.example.tasktracker001.ui.UserViewModelFactory

/**
 * Composable function that defines the navigation graph for the application.
 *
 * It sets up the [NavHost] and defines various composable destinations, including
 * screens for login, signup, dashboard, task details, analytics, and admin panels.
 * It also manages the initialization of ViewModels using their respective factories.
 */
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val database = remember { TaskDatabase.get(context) }
    val userRepository = remember { UserRepository(database.userDao()) }
    val taskRepository = remember { TaskRepository(database.taskDao()) }
    val projectRepository = remember { ProjectRepository(database.projectDao()) }
    val activityLogRepository = remember { ActivityLogRepository(database.activityLogDao()) }

    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userRepository))
    val projectViewModel: ProjectViewModel = viewModel(factory = ProjectViewModelFactory(projectRepository))
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(
            application,
            taskRepository,
            activityLogRepository,
            userRepository
        )
    )

    val loggedInUser = userViewModel.loggedInUser.collectAsState().value

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, userViewModel)
        }
        composable("signup") {
            SignUpScreen(navController, userViewModel)
        }
        composable("forgot_password") { ForgotPasswordScreen() }
        composable("admin_login") {
            AdminLoginScreen(navController, userViewModel)
        }
        composable("admin_signup") {
            AdminSignUpScreen(navController, userViewModel)
        }
        composable("dashboard") { DashboardScreen(navController, userViewModel, taskViewModel) }
        composable("create_task") { CreateTaskScreen(navController, userViewModel, taskViewModel) }
        composable(
            "task_detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            if (taskId != null) {
                TaskDetailScreen(
                    navController,
                    userViewModel,
                    taskViewModel,
                    taskId
                )
            }
        }
        composable("analytics") { AnalyticsScreen(navController, taskViewModel) }
        composable("calendar") { CalendarScreen(navController, taskViewModel) }
        composable("settings") { SettingsScreen(navController, userViewModel) }
        composable("profile") { ProfileScreen(navController, userViewModel) }
        composable("admin_panel") { AdminPanelScreen(navController, userViewModel) }
        composable("admin_user_management") { AdminUserManagementScreen(navController, userViewModel) }
        composable("admin_task_oversight") { AdminTaskOversightScreen(navController, taskViewModel) }
        composable(
            "edit_user/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            if (userId != null) {
                EditUserScreen(
                    navController,
                    userViewModel,
                    userId
                )
            }
        }
        composable("project_dashboard") { ProjectDashboardScreen(navController, projectViewModel) }
        composable(
            "project_detail/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId")
            if (projectId != null) {
                ProjectDetailScreen(
                    navController,
                    projectViewModel,
                    taskViewModel,
                    projectId
                )
            }
        }
    }
}