package com.zihad.surveyform.di

import com.zihad.surveyform.domain.repository.SurveyRepository
import com.zihad.surveyform.domain.usecase.GetSurveyUseCase
import com.zihad.surveyform.domain.usecase.InsertSurveySubmissionUseCase
import com.zihad.surveyform.domain.usecase.GetAllSurveySubmissionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetSurveyUseCase(
        repository: SurveyRepository
    ): GetSurveyUseCase = GetSurveyUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertSurveySubmissionUseCase(
        repository: SurveyRepository
    ): InsertSurveySubmissionUseCase = InsertSurveySubmissionUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllSurveySubmissionsUseCase(
        repository: SurveyRepository
    ): GetAllSurveySubmissionsUseCase = GetAllSurveySubmissionsUseCase(repository)
} 