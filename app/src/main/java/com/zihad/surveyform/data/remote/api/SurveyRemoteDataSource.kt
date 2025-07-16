package com.zihad.surveyform.data.remote.api

import com.zihad.surveyform.data.remote.dto.SurveyResponseDto
import javax.inject.Inject

class SurveyRemoteDataSource @Inject constructor(
    private val apiService: SurveyApiService
) {
    suspend fun fetchSurvey(): SurveyResponseDto {
        return apiService.getSurvey()
    }
} 