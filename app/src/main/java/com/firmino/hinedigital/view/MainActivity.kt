package com.firmino.hinedigital.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firmino.hinedigital.HINEApplication
import com.firmino.hinedigital.view.screens.EvaluationListScreen
import com.firmino.hinedigital.view.screens.EvaluationResumeScreen
import com.firmino.hinedigital.view.screens.ExamScreen
import com.firmino.hinedigital.view.screens.GuideScreen
import com.firmino.hinedigital.view.screens.InformationScreen
import com.firmino.hinedigital.view.screens.MenuScreen
import com.firmino.hinedigital.view.screens.NewEvaluationScreen
import com.firmino.hinedigital.view.theme.HINEDigitalTheme
import com.firmino.hinedigital.viewmodel.EvaluationViewModel
import com.firmino.hinedigital.viewmodel.factory.EvaluationModelViewFactory

class MainActivity : ComponentActivity() {
    private val viewModel: EvaluationViewModel by viewModels { EvaluationModelViewFactory((application as HINEApplication).repository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HINEDigitalTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "menu") {
                    composable("menu") {
                        MenuScreen(
                            navController = navController
                        )
                    }
                    composable("newEvaluation") {
                        NewEvaluationScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable("evaluationList") {
                        EvaluationListScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable("evaluationResume/{id}") {
                        EvaluationResumeScreen(
                            navController = navController,
                            viewModel = viewModel,
                            id = it.arguments?.getString("id")?.toInt()
                        )
                    }
                    composable("exam/{id}/{index}") {
                        ExamScreen(
                            navController = navController,
                            viewModel = viewModel,
                            id = it.arguments?.getString("id")?.toInt(),
                            index = it.arguments?.getString("index")?.toInt() ?: 0
                        )
                    }
                    composable("information") {
                        InformationScreen(
                            navController = navController
                        )
                    }
                    composable("guide") {
                        GuideScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}