package com.example.mysouq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    orderId: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suivi de commande", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Commande #$orderId", 
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Passée le 24 mai 2024", 
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                TrackingStep(
                    title = "Confirmée",
                    date = "24 mai 2024 à 10:30",
                    isCompleted = true,
                    isLast = false
                )
                TrackingStep(
                    title = "Expédiée",
                    date = "25 mai 2024 à 14:20",
                    isCompleted = true,
                    isLast = false
                )
                TrackingStep(
                    title = "En livraison",
                    date = "27 mai 2024 à 09:15",
                    isCompleted = true,
                    isLast = false
                )
                TrackingStep(
                    title = "Livrée",
                    date = "En attente",
                    isCompleted = false,
                    isLast = true
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { /* View details */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Détails de la commande", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TrackingStep(
    title: String,
    date: String,
    isCompleted: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) Color(0xFF4CAF50) 
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                } else {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.outline))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(
                            if (isCompleted) Color(0xFF4CAF50) 
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
        
        Column(modifier = Modifier.padding(start = 16.dp, bottom = 32.dp)) {
            Text(
                text = title, 
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                color = if (isCompleted) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline
            )
            Text(
                text = date, 
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
