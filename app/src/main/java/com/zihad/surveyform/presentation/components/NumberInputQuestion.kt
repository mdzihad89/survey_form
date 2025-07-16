package com.zihad.surveyform.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumberInputQuestion(
    questionText: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = questionText)
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Enter number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
} 