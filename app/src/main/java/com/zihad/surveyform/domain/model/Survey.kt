package com.zihad.surveyform.domain.model

data class Survey(
    val questions: List<Question>
)

data class Question(
    val id: String,
    val type: QuestionType,
    val options: List<Option>?,
    val referToId: String?,
    val skipId: String?,
    val questionText: String?,
    val validation: Validation?
)

data class Option(
    val value: String,
    val referToId: String?
)

data class Validation(
    val regex: String?
)

enum class QuestionType {
    MULTIPLE_CHOICE,
    NUMBER_INPUT,
    DROPDOWN,
    CHECKBOX,
    CAMERA,
    TEXT_INPUT,
    UNKNOWN
} 