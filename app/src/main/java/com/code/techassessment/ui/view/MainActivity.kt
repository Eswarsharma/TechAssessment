package com.code.techassessment.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.code.techassessment.navigation.Home
import com.code.techassessment.navigation.appNavGraph
import com.code.techassessment.ui.theme.TechAssessmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechAssessmentTheme {
                App()
            }
        }
    }
}

/**
 * Composable to host the navigation graph for the app,
 * and display the home screen as the start destination
 */
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        // This is the current navigation graph for the app.
        // I would create individual graphs per feature
        // for example, UserNavGraph, PaymentNavGraph, etc.
        // This structure also supports navigation between features when needed.
        appNavGraph(navController)
    }
}