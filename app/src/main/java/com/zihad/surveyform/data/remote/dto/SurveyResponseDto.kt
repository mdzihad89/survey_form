package com.zihad.surveyform.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
 
@Serializable
data class SurveyResponseDto(
    @SerialName("record")
    val record: List<QuestionDto>
) 