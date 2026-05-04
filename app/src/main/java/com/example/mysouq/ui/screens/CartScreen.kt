package com.example.mysouq.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.ui.common.UiState
import com.example.mysouq.ui.viewmodel.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val total by viewModel.totalPrice.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Votre panier est vide")
                    }
                } else {
                    CartContent(
                        items = state.data,
                        total = total,
                        onUpdateQuantity = viewModel::updateQuantity,
                        onRemove = viewModel::removeFromCart
                    )
                }
            }
            is UiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erreur : ${state.message}")
                }
            }
        }
    }
}

@Composable
fun CartContent(
    items: List<CartItem>,
    total: Double,
    onUpdateQuantity: (Int, Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.product.id }) { item ->
                CartItemRow(
                    item = item,
                    onIncrease = { onUpdateQuantity(item.product.id, item.quantity + 1) },
                    onDecrease = { onUpdateQuantity(item.product.id, item.quantity - 1) },
                    onRemove = { onRemove(item.product.id) }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RectangleShape
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "$total DH",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Checkout */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Commander")
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, style = MaterialTheme.typography.titleMedium)
                Text("${item.product.price} DH", color = MaterialTheme.colorScheme.primary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrease) {
                        Icon(Icons.Default.Remove, contentDescription = "Réduire")
                    }
                    Text("${item.quantity}")
                    IconButton(onClick = onIncrease) {
                        Icon(Icons.Default.Add, contentDescription = "Augmenter")
                    }
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
