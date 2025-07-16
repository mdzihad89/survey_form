package com.zihad.surveyform.di

import com.zihad.surveyform.data.repository.SurveyRepositoryImpl
import com.zihad.surveyform.domain.repository.SurveyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindSurveyRepository(
        impl: SurveyRepositoryImpl
    ): SurveyRepository
} 