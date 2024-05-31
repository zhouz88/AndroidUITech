package com.example.myapplication.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class LearnComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            dosth()
        }
    }


    @Preview
    @Composable
    fun dosth() {
        Row {
            Box(
                Modifier
                    .size(50.dp)
                    .background(color = Color.Red)
            ) {
                Text("纯色", Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.width(100.dp))
            Box(
                Modifier
                    .size(50.dp)
                    .background(brush = ve)
            ) {
                Text(text = "渐变色", Modifier.align(Alignment.Center))
            }
        }
    }

    val ve = Brush.verticalGradient(colors = listOf(
        Color.Red,
        Color.Yellow,
        Color.White
    ))
}