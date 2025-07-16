package com.zihad.surveyform.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey_submissions")
data class SurveySubmissionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val answersJson: String
) 