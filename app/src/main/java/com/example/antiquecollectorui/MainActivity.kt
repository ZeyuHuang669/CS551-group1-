package com.example.antiquecollectorui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.antiquecollectorui.ui.AntiqueApp
import com.example.antiquecollectorui.ui.theme.AntiquecollectoruiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiquecollectoruiTheme {
                AntiqueApp()
            }
        }
    }
}