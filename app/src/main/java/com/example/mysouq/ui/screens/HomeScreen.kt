package com.example.mysouq.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mysouq.domain.model.Category
import com.example.mysouq.domain.model.Product
import com.example.mysouq.ui.common.UiState
import com.example.mysouq.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Sticky Search Bar (Temu Style)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Rechercher un trésor...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { /* Photo Search? */ }) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                ProductGrid(
                    products = state.data,
                    selectedCategory = selectedCategory,
                    onCategorySelect = viewModel::onCategorySelect,
                    onProductClick = onProductClick,
                    onFavoriteClick = viewModel::toggleFavorite,
                    onAddToCartClick = viewModel::addToCart
                )
            }
            is UiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erreur : ")
                }
            }
        }
    }
}

@Composable
fun ProductGrid(
    products: List<Product>,
    selectedCategory: Category?,
    onCategorySelect: (Category?) -> Unit,
    onProductClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onAddToCartClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        // --- 1. Banner Carousel ---
        item(span = { GridItemSpan(2) }) {
            BannerCarousel()
        }

        // --- 2. Categories Circular (Temu style) ---
        item(span = { GridItemSpan(2) }) {
            CategoryStrip(selectedCategory, onCategorySelect)
        }

        // --- 3. Flash Sales Section ---
        item(span = { GridItemSpan(2) }) {
            FlashSaleSection(products.take(3), onProductClick)
        }

        // --- 4. Main Grid Header ---
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Inspiré par vos goûts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        // --- 5. Products ---
        items(products, key = { it.id }) { product ->
            ModernProductCard(
                product = product,
                onClick = { onProductClick(product.id) },
                onFavoriteClick = { onFavoriteClick(product.id) },
                onAddToCartClick = { onAddToCartClick(product.id) }
            )
        }
    }
}

@Composable
fun BannerCarousel() {
    val banners = listOf(
        "Offres du Ramadan : Jusqu'à -50%" to Color(0xFFE91E63),
        "Artisanat de Fès : Edition Limitée" to Color(0xFF3F51B5),
        "Livraison Gratuite dès 500 DH" to Color(0xFF4CAF50)
    )
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentIndex = (currentIndex + 1) % banners.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(banners[currentIndex].second)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = banners[currentIndex].first,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryStrip(
    selectedCategory: Category?,
    onCategorySelect: (Category?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            CategoryItem(
                label = "Tous",
                icon = "🏺",
                isSelected = selectedCategory == null,
                onClick = { onCategorySelect(null) }
            )
        }
        items(Category.entries) { category ->
            CategoryItem(
                label = category.displayName,
                icon = category.icon,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelect(category) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 24.sp)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FlashSaleSection(
    products: List<Product>,
    onProductClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "VENTES FLASH",
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Timer badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text("02:45:12", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(products) { product ->
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable { onProductClick(product.id) }
                    ) {
                        Box {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .background(Color.Red, RoundedCornerShape(bottomEnd = 8.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("-25%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            text = (product.price * 0.75).toString() + " DH",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = product.price.toString() + " DH",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModernProductCard(
    product: Product,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Badge "Artisan"
                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopStart),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Authentique",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (product.isFavorite) Color.Red else Color.White
                    )
                }
            }
            
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                    Text(text = " " + product.rating, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = " (" + product.reviewCount + "+)", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = product.price.toString() + " DH",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                        Text(
                            text = "Livraison Gratuite",
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    IconButton(
                        onClick = onAddToCartClick,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
