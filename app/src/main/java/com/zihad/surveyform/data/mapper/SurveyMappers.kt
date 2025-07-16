package com.zihad.surveyform.data.mapper

import com.zihad.surveyform.data.remote.dto.SurveyResponseDto
import com.zihad.surveyform.data.remote.dto.QuestionDto
import com.zihad.surveyform.data.remote.dto.OptionDto
import com.zihad.surveyform.data.remote.dto.ValidationDto
import com.zihad.surveyform.domain.model.Survey
import com.zihad.surveyform.domain.model.Question
import com.zihad.surveyform.domain.model.Option
import com.zihad.surveyform.domain.model.Validation
import com.zihad.surveyform.domain.model.QuestionType

fun SurveyResponseDto.toDomain(): Survey =
    Survey(
        questions = record.map { it.toDomain() }
    )

fun QuestionDto.toDomain(): Question =
    Question(
        id = id,
        type = when (type) {
            "multipleChoice" -> QuestionType.MULTIPLE_CHOICE
            "numberInput" -> QuestionType.NUMBER_INPUT
            "dropdown" -> QuestionType.DROPDOWN
            "checkbox" -> QuestionType.CHECKBOX
            "camera" -> QuestionType.CAMERA
            "textInput" -> QuestionType.TEXT_INPUT
            else -> QuestionType.UNKNOWN
        },
        options = options?.map { it.toDomain() },
        referToId = referTo?.id,
        skipId = skip?.id,
        questionText = question?.slug,
        validation = validations?.toDomain()
    )

fun OptionDto.toDomain(): Option =
    Option(
        value = value,
        referToId = referTo?.id
    )

fun ValidationDto.toDomain(): Validation =
    Validation(regex = regex) 