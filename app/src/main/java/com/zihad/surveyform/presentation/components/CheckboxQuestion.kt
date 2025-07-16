package com.zihad.surveyform.presentation.survey.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.*

@Composable
fun CheckboxQuestion(
    questionText: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggled: (String) -> Unit
) {
    Column {
        Text(text = questionText)
        options.forEach { option ->
            Row {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = { onOptionToggled(option) }
                )
                Text(option)
            }
        }
    }
} 