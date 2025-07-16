package com.zihad.surveyform.presentation.survey.components

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CameraQuestion(
    questionText: String,
    imageUri: Uri?,
    onCapture: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text(text = questionText)
        if (imageUri == null) {
            Button(onClick = onCapture) {
                Text("Capture Photo")
            }
        } else {
            AsyncImage(
                model = imageUri,
                contentDescription = "Captured image",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
            )
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
} 