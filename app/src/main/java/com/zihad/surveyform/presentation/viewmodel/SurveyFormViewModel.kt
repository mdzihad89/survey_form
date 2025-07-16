package com.zihad.surveyform.presentation.survey.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zihad.surveyform.domain.model.Survey
import com.zihad.surveyform.domain.model.Question
import com.zihad.surveyform.domain.usecase.GetSurveyUseCase
import com.zihad.surveyform.domain.repository.SurveyRepository
import com.zihad.surveyform.data.local.SurveySubmissionEntity
import com.zihad.surveyform.domain.model.QuestionType
import com.zihad.surveyform.domain.usecase.InsertSurveySubmissionUseCase
import com.zihad.surveyform.domain.usecase.GetAllSurveySubmissionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.update


@HiltViewModel
class SurveyFormViewModel @Inject constructor(
    private val getSurveyUseCase: GetSurveyUseCase,
    private val insertSurveySubmissionUseCase: InsertSurveySubmissionUseCase,
    private val getAllSurveySubmissionsUseCase: GetAllSurveySubmissionsUseCase
) : ViewModel() {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val survey: Survey? = null,
        val currentIndex: Int = 0,
        val answers: Map<String, String> = emptyMap(),
        val validationError: String? = null,
        val isSubmit: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState

    private var survey: Survey? = null

    init {
        fetchSurvey()
    }

    fun retry() {
        fetchSurvey()
    }

    private fun fetchSurvey() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch {
            try {
                survey = getSurveyUseCase()
                _uiState.value = UiState(survey = survey)
            } catch (e: Exception) {
                _uiState.value = UiState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun answerCurrentQuestion(answer: String) {
        val currentSurvey = survey ?: return
        val currentQuestion = currentSurvey.questions.getOrNull(_uiState.value.currentIndex) ?: return
        val validationError = validateAnswer(currentQuestion, answer)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(validationError = validationError)
            return
        }
        val newAnswers = _uiState.value.answers.toMutableMap()
        newAnswers[currentQuestion.id] = answer

        val nextIndex = getNextIndex(currentQuestion, answer, currentSurvey)
        _uiState.value = _uiState.value.copy(
            answers = newAnswers,
            currentIndex = nextIndex,
            validationError = null,
            isSubmit = nextIndex == -1
        )
    }

    fun skipCurrentQuestion() {
        val currentSurvey = survey ?: return
        val currentQuestion = currentSurvey.questions.getOrNull(_uiState.value.currentIndex) ?: return
        val skipId = currentQuestion.skipId
        if (skipId != null && skipId != "-1") {
            val skipIdx = currentSurvey.questions.indexOfFirst { it.id == skipId }
            if (skipIdx != -1) {
                _uiState.value = _uiState.value.copy(currentIndex = skipIdx, validationError = null)
            }
        }
    }

    private fun getNextIndex(question: Question, answer: String, survey: Survey): Int {
        return when (question.type) {
            QuestionType.MULTIPLE_CHOICE,
           QuestionType.DROPDOWN -> {
                val nextId = question.options?.find { it.value == answer }?.referToId
                if (nextId == "submit") -1 else survey.questions.indexOfFirst { it.id == nextId }
            }
            else -> {
                val nextId = question.referToId
                if (nextId == "submit") -1 else survey.questions.indexOfFirst { it.id == nextId }
            }
        }
    }

    private fun validateAnswer(question: Question, answer: String): String? {
        return when (question.type) {
            QuestionType.NUMBER_INPUT,
            QuestionType.TEXT_INPUT -> {
                val regex = question.validation?.regex
                if (!regex.isNullOrBlank() && !answer.matches(Regex(regex))) {
                    "Invalid input format."
                } else null
            }
            QuestionType.CHECKBOX -> {
                if (answer.isBlank()) "Please select at least one option." else null
            }
            else -> null
        }
    }

    sealed class SubmitState {
        object Idle : SubmitState()
        object Loading : SubmitState()
        object Success : SubmitState()
        data class Error(val message: String) : SubmitState()
    }

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState

    fun resetSubmitState() {
        _submitState.value = SubmitState.Idle
    }

    fun submit() {
        val answers = _uiState.value.answers
        val entity = SurveySubmissionEntity(
            answersJson = Json.encodeToString(answers)
        )
        viewModelScope.launch {
            _submitState.value = SubmitState.Loading
            try {
                insertSurveySubmissionUseCase(entity)
                _submitState.value = SubmitState.Success
                fetchSurvey()
            } catch (e: Exception) {
                _submitState.value = SubmitState.Error(e.message ?: "Unknown error")

            }
        }
    }
    sealed class ResultState {
        data object Loading : ResultState()
        data class Success(val submissions: List<SurveySubmissionEntity>) : ResultState()
        data class Error(val message: String) : ResultState()
    }

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Loading)
    val resultState: StateFlow<ResultState> = _resultState

    fun loadAllSubmissions() {
        viewModelScope.launch {
            _resultState.value = ResultState.Loading
            try {
                val submissions = getAllSurveySubmissionsUseCase()
                _resultState.value = ResultState.Success(submissions)
            } catch (e: Exception) {
                _resultState.value = ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }

} 