package com.zihad.surveyform.presentation.survey.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultipleChoiceQuestion(
    questionText: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf<String?>(null) }
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = questionText, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        options.forEach { option ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selected == option,
                    onClick = {
                        selected = option
                        onOptionSelected(option)
                    }
                )
                Text(option, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
} 