package com.zihad.surveyform.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SurveySubmissionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(entity: SurveySubmissionEntity)

    @Query("SELECT * FROM survey_submissions ORDER BY id DESC")
    suspend fun getAllSubmissions(): List<SurveySubmissionEntity>
} 