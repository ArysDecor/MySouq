package com.example.mysouq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.ui.common.UiState
import com.example.mysouq.ui.components.ErrorState
import com.example.mysouq.ui.viewmodel.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val total by viewModel.totalPrice.collectAsState()
    val orderCount by viewModel.orderCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    EmptyCart(orderCount)
                } else {
                    CartContent(
                        items = state.data,
                        total = total,
                        orderCount = orderCount,
                        onUpdateQuantity = viewModel::updateQuantity,
                        onRemove = viewModel::removeFromCart,
                        onCheckout = viewModel::checkout
                    )
                }
            }
            is UiState.Error -> {
                ErrorState(
                    message = state.message ?: "Impossible de charger votre panier",
                    onRetry = { /* viewModel.load() */ }
                )
            }
        }
    }
}

@Composable
fun EmptyCart(orderCount: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🛒", style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Votre panier est vide", style = MaterialTheme.typography.titleLarge)
            if (orderCount > 0) {
                Text(
                    "Vous avez déjà passé $orderCount commandes !",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CartContent(
    items: List<CartItem>,
    total: Double,
    orderCount: Int,
    onUpdateQuantity: (Int, Int) -> Unit,
    onRemove: (Int) -> Unit,
    onCheckout: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total", style = MaterialTheme.typography.labelLarge)
                            Text(
                                "$total DH",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Button(
                            onClick = onCheckout,
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(0.6f),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Commander")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Mon Panier (${items.size})",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(items, key = { it.product.id }) { item ->
                CartItemRow(
                    item = item,
                    onIncrease = { onUpdateQuantity(item.product.id, item.quantity + 1) },
                    onDecrease = { onUpdateQuantity(item.product.id, item.quantity - 1) },
                    onRemove = { onRemove(item.product.id) }
                )
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.product.name, 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    "${item.product.price} DH", 
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
                        .padding(horizontal = 4.dp)
                ) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Réduire", modifier = Modifier.size(18.dp))
                    }
                    Text(
                        text = "${item.quantity}",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Augmenter", modifier = Modifier.size(18.dp))
                    }
                }
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    Icons.Default.Delete, 
                    contentDescription = "Supprimer", 
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
