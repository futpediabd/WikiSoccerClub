package com.wikisoccerclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wikisoccerclub.ui.WikiSoccerClubApp
import com.wikisoccerclub.ui.theme.WikiSoccerClubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WikiSoccerClubTheme {
                WikiSoccerClubApp()
            }
        }
    }
}
