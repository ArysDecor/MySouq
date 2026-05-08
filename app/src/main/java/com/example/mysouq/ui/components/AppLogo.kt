package com.example.mysouq.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * AppLogo - Version "Bespoke Artisan"
 * Un logo original qui fusionne l'architecture marocaine (l'arche) 
 * et la géométrie berbère (le losange).
 */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    showText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val sunsetOrange = Color(0xFFFF5722)
        val sunsetCoral = Color(0xFFFF4081)
        val gradient = Brush.linearGradient(listOf(sunsetOrange, sunsetCoral))

        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val strokeWidth = 3.5.dp.toPx()

                // Le concept : Une arche de "Souq" formée par un losange ouvert.
                // Symbolise à la fois l'entrée d'une boutique et la protection.
                val archPath = Path().apply {
                    // Start bottom left (open base)
                    moveTo(w * 0.2f, h * 0.85f)
                    // Up to middle left
                    lineTo(w * 0.1f, h * 0.5f)
                    // Peak (Top center)
                    lineTo(w * 0.5f, h * 0.1f)
                    // Down to middle right
                    lineTo(w * 0.9f, h * 0.5f)
                    // Down to bottom right (open base)
                    lineTo(w * 0.8f, h * 0.85f)
                }

                drawPath(
                    path = archPath,
                    brush = gradient,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Le point central : "La Perle" ou "Le Trésor" au cœur du Souq.
                drawCircle(
                    brush = gradient,
                    radius = 4.dp.toPx(),
                    center = center.copy(y = h * 0.55f)
                )
                
                // Un petit éclat subtil
                drawCircle(
                    color = Color.White,
                    radius = 1.2.dp.toPx(),
                    center = center.copy(x = center.x + 1.5.dp.toPx(), y = h * 0.55f - 1.5.dp.toPx())
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        letterSpacing = (-0.5).sp
                    )) {
                        append("MY")
                    }
                    withStyle(SpanStyle(
                        fontWeight = FontWeight.Light,
                        color = sunsetOrange,
                        letterSpacing = 2.sp
                    )) {
                        append("SOUQ")
                    }
                },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
