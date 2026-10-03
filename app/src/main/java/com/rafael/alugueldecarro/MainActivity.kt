package com.rafael.alugueldecarro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rafael.alugueldecarro.ui.navigation.AppNavigation
import com.rafael.alugueldecarro.ui.theme.AluguelDeCarroTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            AluguelDeCarroTheme {

                AppNavigation()
            }
        }
    }
}