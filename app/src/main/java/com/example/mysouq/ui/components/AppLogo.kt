package com.example.mysouq.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    showText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dégradé "Vibrant Sunset" - Très dynamique pour l'e-commerce
        val gradientColors = listOf(
            Color(0xFFFF5722), // Deep Orange
            Color(0xFFFF4081)  // Pinkish Red/Coral
        )
        val gradient = Brush.linearGradient(gradientColors)

        // L'icône : Un Squircle moderne avec un tracé "S" fluide
        Box(
            modifier = Modifier.size(34.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                
                // Fond Squircle (type iOS/Premium)
                val rect = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = 0f, top = 0f, right = w, bottom = h,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    )
                }
                drawPath(path = rect, brush = gradient)

                // Le "S" de Souq en blanc pur, tracé de manière fluide
                val sPath = Path().apply {
                    moveTo(w * 0.7f, h * 0.3f)
                    cubicTo(w * 0.4f, h * 0.15f, w * 0.2f, h * 0.45f, w * 0.5f, h * 0.5f)
                    cubicTo(w * 0.8f, h * 0.55f, w * 0.6f, h * 0.85f, w * 0.3f, h * 0.7f)
                }

                drawPath(
                    path = sPath,
                    color = Color.White,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
                
                // Petit point "Shopping"
                drawCircle(
                    color = Color.White,
                    radius = 1.5.dp.toPx(),
                    center = Offset(w * 0.75f, h * 0.25f)
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                        append("MY")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Light, color = Color(0xFFFF5722))) {
                        append("SOUQ")
                    }
                },
                fontSize = 22.sp,
                letterSpacing = (-1).sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
