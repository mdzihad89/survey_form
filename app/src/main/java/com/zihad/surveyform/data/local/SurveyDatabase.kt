package com.zihad.surveyform.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SurveySubmissionEntity::class], version = 2)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveySubmissionDao(): SurveySubmissionDao
} 