package com.zihad.surveyform.domain.usecase

import com.zihad.surveyform.data.local.SurveySubmissionEntity
import com.zihad.surveyform.domain.repository.SurveyRepository
import javax.inject.Inject

class InsertSurveySubmissionUseCase @Inject constructor(
    private val repository: SurveyRepository
) {
    suspend operator fun invoke(entity: SurveySubmissionEntity) = repository.insertSubmission(entity)
} 