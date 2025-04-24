package com.example.dollarconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.dollarconverter.screens.DollarConverter
import com.example.dollarconverter.ui.theme.DollarConverterTheme
import com.example.dollarconverter.viewmodel.DollarViewModel


class MainActivity : ComponentActivity() {
    private val viewModel: DollarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DollarConverterTheme {
                DollarConverter(viewModel = viewModel)
            }
        }
    }
}

