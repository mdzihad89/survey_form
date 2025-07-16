package com.zihad.surveyform.presentation.survey.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.*
import com.zihad.surveyform.presentation.survey.viewmodel.SurveyFormViewModel
import com.zihad.surveyform.domain.model.*
import com.zihad.surveyform.presentation.survey.components.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.net.Uri
import android.Manifest
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.zihad.surveyform.presentation.components.NumberInputQuestion
import java.io.File
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import kotlinx.coroutines.launch

@Composable
fun SurveyFormScreen(
    viewModel: SurveyFormViewModel = hiltViewModel(),
    onSurveyResultClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val submitState by viewModel.submitState.collectAsState()

    LaunchedEffect(submitState) {
        when (submitState) {
            is SurveyFormViewModel.SubmitState.Success -> {
                snackbarHostState.showSnackbar("Submission successful!")
                viewModel.resetSubmitState()
            }
            is SurveyFormViewModel.SubmitState.Error -> {
                snackbarHostState.showSnackbar((submitState as SurveyFormViewModel.SubmitState.Error).message)
                viewModel.resetSubmitState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SnackbarHost(hostState = snackbarHostState,modifier = Modifier.align(Alignment.BottomCenter))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSurveyResultClick,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
            ) {
                Text("Survey Result")
            }
            Spacer(modifier = Modifier.height(8.dp))
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = uiState.error ?: "Unknown error")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.survey != null -> {
                    val questions = uiState.survey!!.questions
                    val currentIndex = uiState.currentIndex
                    val currentQuestion = questions.getOrNull(currentIndex)
                    val answers = uiState.answers
                    val validationError = uiState.validationError
                    if (currentQuestion != null) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            when (currentQuestion.type) {
                                QuestionType.MULTIPLE_CHOICE -> {
                                    MultipleChoiceQuestion(
                                        questionText = currentQuestion.questionText ?: "",
                                        options = currentQuestion.options?.map { it.value } ?: emptyList(),
                                        onOptionSelected = { selected ->
                                            viewModel.answerCurrentQuestion(selected)
                                        }
                                    )
                                    if (currentQuestion.skipId != null && currentQuestion.skipId != "-1") {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(onClick = { viewModel.skipCurrentQuestion() }) { Text("Skip") }
                                    }
                                }
                                QuestionType.DROPDOWN -> {
                                    DropdownQuestion(
                                        questionText = currentQuestion.questionText ?: "",
                                        options = currentQuestion.options?.map { it.value } ?: emptyList(),
                                        onOptionSelected = { selected ->
                                            viewModel.answerCurrentQuestion(selected)
                                        }
                                    )
                                    if (currentQuestion.skipId != null && currentQuestion.skipId != "-1") {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(onClick = { viewModel.skipCurrentQuestion() }) { Text("Skip") }
                                    }
                                }
                                QuestionType.NUMBER_INPUT -> {
                                    var value by remember(currentQuestion.id) { mutableStateOf(answers[currentQuestion.id] ?: "") }
                                    NumberInputQuestion(
                                        questionText = currentQuestion.questionText ?: "",
                                        value = value,
                                        onValueChange = { value = it }
                                    )
                                    if (validationError != null) {
                                        Text(validationError, color = MaterialTheme.colorScheme.error)
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Button(onClick = {
                                            viewModel.answerCurrentQuestion(value)
                                        }) { Text("Next") }
                                        if (currentQuestion.skipId != null && currentQuestion.skipId != "-1") {
                                            Button(onClick = { viewModel.skipCurrentQuestion() }) { Text("Skip") }
                                        }
                                    }
                                }
                                QuestionType.TEXT_INPUT -> {
                                    var value by remember(currentQuestion.id) { mutableStateOf(answers[currentQuestion.id] ?: "") }
                                    TextInputQuestion(
                                        questionText = currentQuestion.questionText ?: "",
                                        value = value,
                                        onValueChange = { value = it }
                                    )
                                    if (validationError != null) {
                                        Text(validationError, color = MaterialTheme.colorScheme.error)
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Button(onClick = {
                                            viewModel.answerCurrentQuestion(value)
                                        }) { Text("Next") }
                                        if (currentQuestion.skipId != null && currentQuestion.skipId != "-1") {
                                            Button(onClick = { viewModel.skipCurrentQuestion() }) { Text("Skip") }
                                        }
                                    }
                                }
                                QuestionType.CHECKBOX -> {
                                    val options = currentQuestion.options?.map { it.value } ?: emptyList()
                                    var selected by remember(currentQuestion.id) {
                                        mutableStateOf(
                                            answers[currentQuestion.id]?.split(",")?.toSet() ?: setOf()
                                        )
                                    }
                                    CheckboxQuestion(
                                        questionText = currentQuestion.questionText ?: "",
                                        options = options,
                                        selectedOptions = selected,
                                        onOptionToggled = { opt ->
                                            selected = if (selected.contains(opt)) selected - opt else selected + opt
                                        }
                                    )
                                    if (validationError != null) {
                                        Text(validationError, color = MaterialTheme.colorScheme.error)
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Button(onClick = {
                                            viewModel.answerCurrentQuestion(selected.joinToString(","))
                                        }) { Text("Next") }
                                        if (currentQuestion.skipId != null && currentQuestion.skipId != "-1") {
                                            Button(onClick = { viewModel.skipCurrentQuestion() }) { Text("Skip") }
                                        }
                                    }
                                }
                                QuestionType.CAMERA -> {
                                    val qid = currentQuestion.id
                                    var imageUri by remember(qid) { mutableStateOf<Uri?>(null) }
                                    var hasImage by remember(qid) { mutableStateOf(false) }
                                    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                                        hasImage = success && imageUri != null
                                    }
                                    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                                        if (granted) {
                                            val uri = createImageFile(context, qid)
                                            imageUri = uri
                                            cameraLauncher.launch(uri)
                                        } else {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Camera permission is required to take photos.")
                                            }
                                        }
                                    }
                                    CameraQuestion(
                                        questionText = currentQuestion.questionText ?: "",
                                        imageUri = if (hasImage) imageUri else null,
                                        onCapture = {  permissionLauncher.launch(Manifest.permission.CAMERA) },
                                        onNext = {
                                            viewModel.answerCurrentQuestion(imageUri?.toString() ?: "")
                                            imageUri = null
                                            hasImage = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if (currentQuestion.skipId != null && currentQuestion.skipId != "-1") {
                                        Button(onClick = { viewModel.skipCurrentQuestion() }) { Text("Skip") }
                                    }
                                }
                                else -> {
                                    Text("Unsupported question type")
                                }
                            }
                        }
                    } else if (uiState.isSubmit) {

                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("All done! Review your answers:")
                                Spacer(modifier = Modifier.height(16.dp))
                                answers.forEach { (qid, ans) ->
                                    Text("$qid: $ans")
                                }
                            }
                            if (submitState is SurveyFormViewModel.SubmitState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            Button(
                                onClick = { viewModel.submit() },
                                enabled = submitState is SurveyFormViewModel.SubmitState.Idle,
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .padding(24.dp)
                                    .align(Alignment.BottomCenter)
                            ) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun createImageFile(context: Context, qid: String): Uri {
    val file = File(context.cacheDir, "camera_${qid}_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
} 