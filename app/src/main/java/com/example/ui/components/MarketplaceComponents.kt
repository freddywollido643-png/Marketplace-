package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.entity.ProductEntity
import com.example.data.entity.UserEntity
import com.example.data.model.DEFAULT_CATEGORIES
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen
import com.example.ui.theme.MalagasyGreenDark
import java.text.NumberFormat
import java.util.Locale

// Price Formatter for Ariary (Ar)
fun formatAriary(amount: Long): String {
    val formatter = NumberFormat.getInstance(Locale.FRANCE)
    return "${formatter.format(amount)} Ar"
}

@Composable
fun OfflineNotificationBanner(
    isOffline: Boolean,
    onOpenSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isOffline,
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("offline_network_banner")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = "Tsy misy internet",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tsy misy fifandraisana Internet",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color(0xFF991B1B)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Mba manokafa Data finday na Wi-Fi hahafahanao mampiasa ny TSENA MALAGASY.",
                        fontSize = 11.sp,
                        color = Color(0xFF7F1D1D),
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onOpenSettingsClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("open_network_settings_button")
                ) {
                    Text(
                        text = "Manokatra",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHeader(
    currentUser: UserEntity?,
    unreadNotificationCount: Int,
    unreadMessageCount: Int,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAccountSwitchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MalagasyGreen,
        contentColor = Color.White,
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        shadowElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Slogan
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.clickable { onProfileClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MalagasyGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Logo",
                            tint = MalagasyGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "TONGASOA ETO AMIN'NY",
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "TSENA MALAGASY",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = MalagasyGold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Top Action Buttons (Bento Circles)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Notification bell with badge
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            .clickable { onNotificationsClick() }
                            .testTag("top_notification_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        BadgedBox(badge = {
                            if (unreadNotificationCount > 0) {
                                Badge(containerColor = MalagasyGold, contentColor = Color.Black) {
                                    Text(unreadNotificationCount.toString())
                                }
                            }
                        }) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Fampandrenesana", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Messages icon with badge
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            .clickable { onMessagesClick() }
                            .testTag("top_messages_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        BadgedBox(badge = {
                            if (unreadMessageCount > 0) {
                                Badge(containerColor = MalagasyGold, contentColor = Color.Black) {
                                    Text(unreadMessageCount.toString())
                                }
                            }
                        }) {
                            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Hafatra", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Profile / Account Switcher Circle
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MalagasyGold)
                            .clickable { onAccountSwitchClick() }
                            .testTag("account_switch_chip"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentUser?.accountType?.take(2) ?: "GE"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = MalagasyGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Search Input in TopBar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSearchClick() }
                    .testTag("top_search_bar_input")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Fikarohana", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Inona no tadiavinao ?",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCardItem(
    product: ProductEntity,
    isFavorite: Boolean,
    onProductClick: (ProductEntity) -> Unit,
    onFavoriteClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick(product) }
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF1F5F9))
            ) {
                AsyncImage(
                    model = product.imageUrls.split(",").firstOrNull() ?: "",
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Condition Tag & Featured Gold Tag
                Row(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.isFeatured) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MalagasyGold,
                            contentColor = Color.Black
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.Black, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "GOLD",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (product.condition == "Neuf") MalagasyGreen else Color(0xFFF59E0B),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = product.condition.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Favorite Button
                IconButton(
                    onClick = { onFavoriteClick(product) },
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopEnd)
                        .size(30.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .testTag("favorite_button_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Tian'ny olona",
                        tint = if (isFavorite) Color(0xFFEF4444) else Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Status Badge if not APPROVED
                if (product.status != "APPROVED") {
                    Surface(
                        color = Color.Black.copy(alpha = 0.8f),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = when (product.status) {
                                "PENDING_REVIEW" -> "Miandry fanamarinana"
                                "SOLD" -> "EFA LAFO ✅"
                                "REJECTED" -> "Tsy nekena"
                                else -> product.status
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 2.dp)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatAriary(product.price),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = MalagasyGreen
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Toerana",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${product.city}, ${product.region.split(" ").firstOrNull() ?: ""}",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (product.sellerVerified) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Mpivarotra voamarina",
                            tint = MalagasyGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Mpivarotra voamarina",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MalagasyGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SellerApprovalBanner(
    currentUser: UserEntity?,
    onContactWhatsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if (currentUser != null && currentUser.accountType == "SELLER" && currentUser.sellerStatus == "PENDING_APPROVAL") {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MalagasyGold),
            modifier = modifier
                .fillMaxWidth()
                .testTag("seller_approval_banner")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MalagasyGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockClock,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Fankatoavana mpivarotra ilaina",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFFB45309)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mba hahafahanao mivarotra ao amin'ny TSENA MALAGASY dia mila ankatoavin'ny administrateur aloha ianao.",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val whatsappUrl = "https://api.whatsapp.com/send?phone=261385651378&text=Salama%20administrateur%20TSENA%20MALAGASY,%20te%20hafy%20hanamarina%20kaonty%20mpivarotra%20aho."
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("whatsapp_seller_approval_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mifandraisa aminay amin'ny WhatsApp (+261 38 56 513 78)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChipRow(
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Rehetra (All)", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(14.dp),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedCategory == null,
                    borderColor = Color(0xFFE5E7EB),
                    selectedBorderColor = MalagasyGreen
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = Color(0xFF475569),
                    selectedContainerColor = MalagasyGreen,
                    selectedLabelColor = Color.White
                )
            )
        }

        items(DEFAULT_CATEGORIES) { cat ->
            val isSel = selectedCategory == cat.nameMg
            FilterChip(
                selected = isSel,
                onClick = {
                    onCategorySelected(if (isSel) null else cat.nameMg)
                },
                label = { Text(cat.nameMg, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium) },
                shape = RoundedCornerShape(14.dp),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSel,
                    borderColor = Color(0xFFE5E7EB),
                    selectedBorderColor = MalagasyGreen
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = Color(0xFF475569),
                    selectedContainerColor = MalagasyGreen,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
