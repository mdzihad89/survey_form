package com.zihad.surveyform.presentation.survey.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TextInputQuestion(
    questionText: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = questionText)
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Enter text") },
           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
    }
} 