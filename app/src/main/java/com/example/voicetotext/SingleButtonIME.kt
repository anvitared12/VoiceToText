package com.example.voicetotext

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.speech.RecognizerIntent
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp

class SingleButtonIME : InputMethodService() {

    override fun onCreateInputView(): View {
        return ComposeView(this).apply {
            setContent {
                MaterialTheme(colorScheme = darkColorScheme()) {
                    MicrophoneKeyboard(
                        onMicClick = {
                            startVoiceRecognition()
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun MicrophoneKeyboard(
        onMicClick: () -> Unit
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = onMicClick,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_mic_none_24),
                        contentDescription = "Voice Input",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    private fun startVoiceRecognition() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("VOICE_RECOGNITION_INTENT", intent)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(mainActivityIntent)

        } catch (e: Exception) {

            currentInputConnection?.commitText("Voice recognition not available", 1)
        }
    }
}