package com.zihad.surveyform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zihad.surveyform.presentation.survey.screens.SurveySubmissionResultScreen
import com.zihad.surveyform.presentation.survey.screens.SurveyFormScreen

import com.zihad.surveyform.ui.theme.SurveyFormTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SurveyFormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SurveyNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SurveyNavHost(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "form",
        modifier = modifier
    ) {
        composable("form") {
            SurveyFormScreen(
                onSurveyResultClick = { navController.navigate("result") }
            )
        }
        composable("result") {
            SurveySubmissionResultScreen()
        }
    }
}