package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.entity.ProductEntity
import com.example.ui.components.OrderAndMobilePaymentModal
import com.example.ui.components.ReportProductDialog
import com.example.ui.components.formatAriary
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen
import com.example.ui.theme.MalagasyGreenDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: ProductEntity,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onContactSeller: (sellerId: String, sellerName: String, productId: String, productTitle: String, price: Long, image: String) -> Unit,
    onSubmitReport: (reason: String, description: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showReportDialog by remember { mutableStateOf(false) }
    var showOrderModal by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(text = product.title, maxLines = 1) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Miverina")
                }
            },
            actions = {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favoris",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
                IconButton(onClick = { showReportDialog = true }) {
                    Icon(Icons.Default.Report, contentDescription = "Signaler", tint = Color.Gray)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            // Product Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color(0xFFE2E8F0))
            ) {
                AsyncImage(
                    model = product.imageUrls.split(",").firstOrNull() ?: "",
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (product.condition == "Neuf") MalagasyGreen else Color(0xFFE65100),
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = product.condition,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Main Product Details Info
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatAriary(product.price),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = MalagasyGreen
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Category, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(product.category, fontSize = 12.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${product.city}, ${product.region.split(" ").firstOrNull()}", fontSize = 12.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${product.views} views", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Mpanazava (Description)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = product.description, fontSize = 14.sp, lineHeight = 20.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // Seller Card Profile
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MalagasyGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = product.sellerName.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = product.sellerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    if (product.sellerVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "Mpivarotra voamarina",
                                            tint = MalagasyGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = if (product.sellerVerified) "Mpivarotra voamarina ✅" else "Mpivarotra ao amin'ny tsena",
                                    fontSize = 12.sp,
                                    color = MalagasyGreen
                                )

                                Text(
                                    text = "Tel: ${product.sellerPhone}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Call & WhatsApp buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${product.sellerPhone}"))
                                    context.startActivity(dialIntent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Appeler")
                            }

                            Button(
                                onClick = {
                                    onContactSeller(
                                        product.sellerId,
                                        product.sellerName,
                                        product.id,
                                        product.title,
                                        product.price,
                                        product.imageUrls.split(",").firstOrNull() ?: ""
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Hafatra (Chat)", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buy / Place Order Button
                Button(
                    onClick = { showOrderModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MalagasyGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("buy_product_button")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hividy izao (Buy Now)",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    if (showReportDialog) {
        ReportProductDialog(
            targetTitle = product.title,
            onSubmit = onSubmitReport,
            onDismiss = { showReportDialog = false }
        )
    }

    if (showOrderModal) {
        OrderAndMobilePaymentModal(
            productTitle = product.title,
            productPrice = product.price,
            sellerName = product.sellerName,
            onConfirmOrder = { address, method ->
                showOrderModal = false
            },
            onDismiss = { showOrderModal = false }
        )
    }
}
