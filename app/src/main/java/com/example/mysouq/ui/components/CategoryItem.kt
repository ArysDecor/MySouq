package com.example.mysouq.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.foundation.border
import com.example.mysouq.ui.theme.ArtisanCream

@Composable
fun CategoryItem(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                onClick = onClick,
                onClickLabel = "Sélectionner $label"
            )
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color(0xFFFFE0B2)
                    else Color.White
                )
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) Color(0xFFFF5722) else MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon, 
                fontSize = 32.sp,
                modifier = Modifier.clearAndSetSemantics { }
            )
        }
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) Color(0xFFE65100) else MaterialTheme.colorScheme.outline,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp),
            letterSpacing = 1.sp
        )
    }
}
