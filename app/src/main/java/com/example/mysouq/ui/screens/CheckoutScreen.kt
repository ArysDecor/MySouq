package com.example.mysouq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysouq.ui.theme.ArtisanCream
import com.example.mysouq.ui.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isOrderPlaced) {
        if (uiState.isOrderPlaced) {
            onOrderSuccess()
        }
    }

    Scaffold(
        containerColor = ArtisanCream,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CONFIRMATION", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Button(
                    onClick = { viewModel.placeOrder() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Confirmer l'achat • ${uiState.total} DH", fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Section 1: Livraison
            CheckoutSectionTitle("ADRESSE DE LIVRAISON")
            OutlinedTextField(
                value = uiState.selectedAddress,
                onValueChange = { viewModel.onAddressChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: 123 Rue de la Médina, Marrakech") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF5722),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Section 2: Paiement
            CheckoutSectionTitle("MODE DE PAIEMENT")
            PaymentMethodSelector(
                selectedMethod = uiState.selectedPaymentMethod,
                onMethodSelect = { viewModel.onPaymentMethodChange(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Section 3: Récapitulatif
            CheckoutSectionTitle("RÉCAPITULATIF")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    uiState.cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.product.name}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            Text("${item.product.price * item.quantity} DH", fontWeight = FontWeight.Bold)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                        Text("${uiState.total} DH", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFFFF5722))
                    }
                }
            }
            
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun CheckoutSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(bottom = 12.dp),
        letterSpacing = 1.sp
    )
}

@Composable
fun PaymentMethodSelector(
    selectedMethod: String,
    onMethodSelect: (String) -> Unit
) {
    val methods = listOf("Carte Bancaire", "Paiement à la livraison", "PayPal")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        methods.forEach { method ->
            val isSelected = method == selectedMethod
            Surface(
                onClick = { onMethodSelect(method) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) Color(0xFFFFE0B2).copy(alpha = 0.3f) else Color.White,
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) Color(0xFFFF5722) else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF5722))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = method, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium)
                }
            }
        }
    }
}
