package com.zihad.surveyform.domain.repository

import com.zihad.surveyform.domain.model.Survey
import com.zihad.surveyform.data.local.SurveySubmissionEntity

interface SurveyRepository {
    suspend fun getSurvey(): Survey
    suspend fun insertSubmission(entity: SurveySubmissionEntity)
    suspend fun getAllSubmissions(): List<SurveySubmissionEntity>
} 