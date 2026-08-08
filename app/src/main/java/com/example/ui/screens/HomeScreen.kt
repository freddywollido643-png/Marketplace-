package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ProductEntity
import com.example.data.entity.UserEntity
import com.example.data.model.DEFAULT_CATEGORIES
import com.example.ui.components.CategoryChipRow
import com.example.ui.components.ProductCardItem
import com.example.ui.components.SellerApprovalBanner
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen
import com.example.ui.theme.MalagasyGreenDark

@Composable
fun HomeScreen(
    currentUser: UserEntity?,
    approvedProducts: List<ProductEntity>,
    favoriteProductIds: Set<String>,
    onProductClick: (ProductEntity) -> Unit,
    onFavoriteClick: (ProductEntity) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSellClick: () -> Unit,
    onContactWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    val filteredProducts = remember(approvedProducts, selectedCategoryFilter) {
        if (selectedCategoryFilter == null) approvedProducts
        else approvedProducts.filter { it.category == selectedCategoryFilter }
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 90.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Hero Section (Bento Green Canvas Header)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(MalagasyGreenDark, MalagasyGreen)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MalagasyGold,
                        contentColor = MalagasyGreen,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "TSENA NOMERIKA MALAGASY 🇲🇬",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                        )
                    }

                    Text(
                        text = "Mitadiava sy mivarotra mora eto Madagasikara",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tsena nomerika mampifandray mpividy sy mpivarotra Malagasy.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Search Bar Box
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSearchClick() }
                            .testTag("hero_search_box")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MalagasyGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Inona no tadiavinao ?",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero Action Buttons (Hividy & Hivarotra)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onSearchClick,
                            colors = ButtonDefaults.buttonColors(containerColor = MalagasyGold),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_hividy_button")
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = MalagasyGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hividy", color = MalagasyGreen, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }

                        Button(
                            onClick = onSellClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_hivarotra_button")
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = MalagasyGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hivarotra", color = MalagasyGreen, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Seller Approval Banner if User is Pending Seller
        item {
            SellerApprovalBanner(
                currentUser = currentUser,
                onContactWhatsApp = onContactWhatsApp,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Categories Section - Bento Grid Icons
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sokajy malaza (Categories)",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color(0xFF0F172A)
                    )
                    TextButton(onClick = { onSearchClick() }) {
                        Text(
                            text = "Jereo daholo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MalagasyGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Bento Grid 4-column squircle cards
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DEFAULT_CATEGORIES.take(4).forEach { cat ->
                        val isSelected = selectedCategoryFilter == cat.nameMg
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MalagasyGreen else Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) MalagasyGreen else Color(0xFFE5E7EB)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedCategoryFilter = if (isSelected) null else cat.nameMg
                                    onCategoryClick(cat.nameMg)
                                }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(vertical = 12.dp, horizontal = 4.dp)
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (isSelected) MalagasyGold else Color(0xFFF1F5F9)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (cat.id) {
                                            "cat_1" -> Icons.Default.PhoneAndroid
                                            "cat_2" -> Icons.Default.Computer
                                            "cat_3" -> Icons.Default.Checkroom
                                            "cat_4" -> Icons.Default.Home
                                            else -> Icons.Default.Category
                                        },
                                        contentDescription = cat.nameMg,
                                        tint = MalagasyGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = cat.nameMg,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White else Color(0xFF0F172A),
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Filter chips row
        item {
            Spacer(modifier = Modifier.height(8.dp))
            CategoryChipRow(
                selectedCategory = selectedCategoryFilter,
                onCategorySelected = { selectedCategoryFilter = it }
            )
        }

        // Recent / Featured Listings Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Entana vao tonga (Recent Products)",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Vokatra nekena sy ampiasaina ao amin'ny tsenanay",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                TextButton(onClick = onSearchClick) {
                    Text("Jereo Rehetra", color = MalagasyGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Product Cards Grid
        if (filteredProducts.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tsy mbola misy entana eto.", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Mbolatsy misy entana mifanaraka amin'ny fikarohanao.", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        } else {
            items(filteredProducts.chunked(2)) { pair ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    pair.forEach { prod ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCardItem(
                                product = prod,
                                isFavorite = favoriteProductIds.contains(prod.id),
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

        // Delivery & Payment Features Announcement Card - Bento Horizontal Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MalagasyGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = MalagasyGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Fandefasana sy Fandoavam-bola",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "MVola, Orange Money, Airtel Money sy fandefasana entana manerana an'i Madagasikara.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}
