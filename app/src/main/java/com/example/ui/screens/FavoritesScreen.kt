package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.ProductEntity
import com.example.ui.components.ProductCardItem
import com.example.ui.theme.MalagasyGreen

@Composable
fun FavoritesScreen(
    favorites: List<FavoriteEntity>,
    allProducts: List<ProductEntity>,
    onProductClick: (ProductEntity) -> Unit,
    onFavoriteClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteProductIds = remember(favorites) { favorites.map { it.productId }.toSet() }
    val favoriteProducts = remember(allProducts, favoriteProductIds) {
        allProducts.filter { favoriteProductIds.contains(it.id) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Tian'ny olona (Favorites)",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MalagasyGreen
        )
        Text(
            text = "Ireo entana notahirizinao hankafizina sy hovidiana indray",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteProducts.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tsy mbola manana favoris ianao.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tsindrio ny fo fotsy amin'ny entana tianao hitahirizana azy.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(favoriteProducts.chunked(2)) { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        pair.forEach { prod ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCardItem(
                                    product = prod,
                                    isFavorite = true,
                                    onProductClick = onProductClick,
                                    onFavoriteClick = { onFavoriteClick(prod) }
                                )
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
