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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mysouq.domain.model.Category
import com.example.mysouq.domain.model.Product
import com.example.mysouq.ui.common.UiState
import com.example.mysouq.ui.components.CategoryItem
import com.example.mysouq.ui.components.ErrorState
import com.example.mysouq.ui.components.ProductCard
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sticky Search Bar
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
                placeholder = { Text("Rechercher un trésor...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
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
                ErrorState(
                    message = state.message ?: "Impossible de charger les produits",
                    onRetry = { /* viewModel.load() */ }
                )
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
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(2) }) {
            BannerCarousel()
        }

        item(span = { GridItemSpan(2) }) {
            CategoryStrip(selectedCategory, onCategorySelect)
        }

        item(span = { GridItemSpan(2) }) {
            FlashSaleSection(products.take(3), onProductClick)
        }

        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Inspiré par vos goûts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(products, key = { it.id }) { product ->
            ProductCard(
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
        "Offres du Ramadan : Jusqu'à -50%" to MaterialTheme.colorScheme.primary,
        "Artisanat de Fès : Edition Limitée" to MaterialTheme.colorScheme.secondary,
        "Livraison Gratuite dès 500 DH" to Color(0xFF4CAF50)
    )
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % banners.size
        }
    }

    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "BannerTransition"
    ) { index ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(banners[index].second)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = banners[index].first,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CategoryStrip(
    selectedCategory: Category?,
    onCategorySelect: (Category?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
fun FlashSaleSection(
    products: List<Product>,
    onProductClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "VENTES FLASH",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "02:45:12",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 12.dp)
            ) {
                items(products) { product ->
                    FlashSaleItem(product, onProductClick)
                }
            }
        }
    }
}

@Composable
fun FlashSaleItem(product: Product, onProductClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clickable { onProductClick(product.id) }
    ) {
        Box {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(94.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Surface(
                color = Color.Red,
                shape = RoundedCornerShape(bottomEnd = 8.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    "-25%",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        Text(
            text = "${(product.price * 0.75).toInt()} DH",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${product.price} DH",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelSmall,
            textDecoration = TextDecoration.LineThrough
        )
    }
}
