package com.zihad.surveyform.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    @SerialName("id")
    val id: String,
    @SerialName("skip")
    val skip: SkipDto? = null,
    @SerialName("type")
    val type: String,
    @SerialName("options")
    val options: List<OptionDto>? = null,
    @SerialName("referTo")
    val referTo: ReferToDto? = null,
    @SerialName("question")
    val question: QuestionTextDto? = null,
    @SerialName("validations")
    val validations: ValidationDto? = null
)

@Serializable
data class SkipDto(
    @SerialName("id")
    val id: String? = null
)

@Serializable
data class OptionDto(
    @SerialName("value")
    val value: String,
    @SerialName("referTo")
    val referTo: ReferToDto? = null
)

@Serializable
data class ReferToDto(
    @SerialName("id")
    val id: String? = null
)

@Serializable
data class QuestionTextDto(
    @SerialName("slug")
    val slug: String? = null
)

@Serializable
data class ValidationDto(
    @SerialName("regex")
    val regex: String? = null
) 