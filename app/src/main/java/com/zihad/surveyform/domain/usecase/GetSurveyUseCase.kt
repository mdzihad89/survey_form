package com.zihad.surveyform.domain.usecase

import com.zihad.surveyform.domain.model.Survey
import com.zihad.surveyform.domain.repository.SurveyRepository
import javax.inject.Inject

class GetSurveyUseCase @Inject constructor(
    private val repository: SurveyRepository
) {
    suspend operator fun invoke(): Survey {
        return repository.getSurvey()
    }
} 