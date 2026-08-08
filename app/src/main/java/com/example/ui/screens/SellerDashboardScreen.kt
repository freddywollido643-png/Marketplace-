package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ProductEntity
import com.example.data.entity.UserEntity
import com.example.ui.components.SellerApprovalBanner
import com.example.ui.components.formatAriary
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@Composable
fun SellerDashboardScreen(
    currentUser: UserEntity?,
    sellerProducts: List<ProductEntity>,
    onAddProductClick: () -> Unit,
    onMarkAsSold: (String) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onContactWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (currentUser == null) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
            Text("Mila miditra kaonty ianao hihiratra amin'ny Dashboard Mpivarotra.")
        }
        return
    }

    val totalListings = sellerProducts.size
    val activeListings = sellerProducts.count { it.status == "APPROVED" }
    val pendingListings = sellerProducts.count { it.status == "PENDING_REVIEW" }
    val soldListings = sellerProducts.count { it.status == "SOLD" }
    val totalViews = sellerProducts.sumOf { it.views }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        item {
            Text(
                text = "Tableau de Bord Mpivarotra",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MalagasyGreen
            )
            Text(
                text = "Tantanana eto ny entanao sy ny antontan'isa momba ny varotra",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Seller Approval Banner if NOT approved
        if (currentUser.sellerStatus != "APPROVED") {
            item {
                SellerApprovalBanner(
                    currentUser = currentUser,
                    onContactWhatsApp = onContactWhatsApp
                )
            }
        } else {
            // Stats Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard("Entana rehetra", totalListings.toString(), Icons.Default.Inventory, Modifier.weight(1f))
                        StatCard("Mandeha (Approved)", activeListings.toString(), Icons.Default.CheckCircle, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard("Miandry (Pending)", pendingListings.toString(), Icons.Default.HourglassTop, Modifier.weight(1f))
                        StatCard("Efa lafo (Sold)", soldListings.toString(), Icons.Default.Sell, Modifier.weight(1f))
                    }
                }
            }

            // Add Product Button Action
            item {
                Button(
                    onClick = onAddProductClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("seller_add_product_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hampiditra entana vaovao", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Products Management Header
            item {
                Text(
                    text = "Entako hamidy (My Products)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }

            // Products List
            if (sellerProducts.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text("Tsy mbola manana entana hapetraka ianao.", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tsindrio ny bokotra eo ambony hampidirana entana vaovao.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            } else {
                items(sellerProducts) { product ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(formatAriary(product.price), fontWeight = FontWeight.ExtraBold, color = MalagasyGreen, fontSize = 14.sp)
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = when (product.status) {
                                        "APPROVED" -> MalagasyGreen
                                        "PENDING_REVIEW" -> MalagasyGold
                                        "SOLD" -> Color(0xFF1E88E5)
                                        else -> Color.Gray
                                    },
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = product.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Jery: ${product.views} • Favoris: ${product.favoritesCount}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (product.status != "SOLD") {
                                        TextButton(onClick = { onMarkAsSold(product.id) }) {
                                            Text("Avoaka ho lafo", fontSize = 11.sp, color = MalagasyGreen, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    IconButton(onClick = { onDeleteProduct(product.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Fafao", tint = Color.Red, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MalagasyGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(text = title, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}
