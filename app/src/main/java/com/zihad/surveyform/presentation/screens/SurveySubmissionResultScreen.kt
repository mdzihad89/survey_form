package com.zihad.surveyform.presentation.survey.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zihad.surveyform.presentation.survey.viewmodel.SurveyFormViewModel
import kotlinx.serialization.json.Json
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SurveySubmissionResultScreen(viewModel: SurveyFormViewModel = hiltViewModel()) {
    val resultState by viewModel.resultState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllSubmissions()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = resultState) {
            is SurveyFormViewModel.ResultState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SurveyFormViewModel.ResultState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is SurveyFormViewModel.ResultState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.submissions) { submission ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Submission ID: ${submission.id}", style = MaterialTheme.typography.labelMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                val answers: Map<String, String> = try {
                                    Json.decodeFromString(submission.answersJson)
                                } catch (e: Exception) { emptyMap() }
                                answers.forEach { (qid, ans) ->
                                    Text("$qid: $ans", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 