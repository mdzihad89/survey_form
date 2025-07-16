package com.zihad.surveyform.di

import android.content.Context
import androidx.room.Room
import com.zihad.surveyform.data.local.SurveyDatabase
import com.zihad.surveyform.data.local.SurveySubmissionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSurveyDatabase(@ApplicationContext context: Context): SurveyDatabase =
        Room.databaseBuilder(
            context,
            SurveyDatabase::class.java,
            "survey_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    @Singleton
    fun provideSurveySubmissionDao(db: SurveyDatabase): SurveySubmissionDao =
        db.surveySubmissionDao()
} 