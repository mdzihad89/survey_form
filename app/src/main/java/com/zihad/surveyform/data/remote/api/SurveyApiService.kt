package com.zihad.surveyform.data.remote.api

import com.zihad.surveyform.data.remote.dto.SurveyResponseDto
import retrofit2.http.GET
 
interface SurveyApiService {
    @GET("b/687374506063391d31aca23a")
    suspend fun getSurvey(): SurveyResponseDto
} 