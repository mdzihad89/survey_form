package com.zihad.surveyform.data.repository

import com.zihad.surveyform.data.remote.api.SurveyRemoteDataSource
import com.zihad.surveyform.domain.model.Survey
import com.zihad.surveyform.domain.repository.SurveyRepository
import com.zihad.surveyform.data.remote.dto.SurveyResponseDto
import com.zihad.surveyform.data.local.SurveySubmissionDao
import com.zihad.surveyform.data.local.SurveySubmissionEntity
import com.zihad.surveyform.data.mapper.toDomain
import javax.inject.Inject

class SurveyRepositoryImpl @Inject constructor(
    private val remoteDataSource: SurveyRemoteDataSource,
    private val submissionDao: SurveySubmissionDao
) : SurveyRepository {
    override suspend fun getSurvey(): Survey {
        val dto: SurveyResponseDto = remoteDataSource.fetchSurvey()
        return dto.toDomain()
    }

    override suspend fun insertSubmission(entity: SurveySubmissionEntity) = submissionDao.insertSubmission(entity)
    override suspend fun getAllSubmissions(): List<SurveySubmissionEntity> = submissionDao.getAllSubmissions()
} 