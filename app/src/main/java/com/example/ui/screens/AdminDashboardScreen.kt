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
import com.example.data.entity.MonetizationTransactionEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReportEntity
import com.example.data.entity.UserEntity
import com.example.ui.components.MonetizationRevenueSummaryCard
import com.example.ui.components.formatAriary
import com.example.ui.components.formatAriarySimple
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@Composable
fun AdminDashboardScreen(
    pendingSellers: List<UserEntity>,
    pendingProducts: List<ProductEntity>,
    allUsers: List<UserEntity>,
    allProducts: List<ProductEntity>,
    allReports: List<ReportEntity>,
    monetizationTransactions: List<MonetizationTransactionEntity> = emptyList(),
    onApproveSeller: (String) -> Unit,
    onRejectSeller: (String) -> Unit,
    onApproveProduct: (String) -> Unit,
    onRejectProduct: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Mpivarotra", "Entana", "Mpanjifa", "Fitarainana", "Revenus 💰")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        // Admin Top Bar
        Surface(color = Color(0xFF0F241E), contentColor = Color.White) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ADMINISTRATEUR TSENA MALAGASY",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = MalagasyGold
                )
                Text(
                    text = "Fitantanana mpivarotra, entana, mpanjifa ary tatitra",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> SellerRequestsTab(pendingSellers, onApproveSeller, onRejectSeller)
            1 -> ProductModerationTab(pendingProducts, onApproveProduct, onRejectProduct)
            2 -> UserManagementTab(allUsers)
            3 -> ReportsManagementTab(allReports)
            4 -> AdminRevenuesTab(monetizationTransactions)
        }
    }
}

@Composable
fun SellerRequestsTab(
    pendingSellers: List<UserEntity>,
    onApproveSeller: (String) -> Unit,
    onRejectSeller: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Fangatahana Mpivarotra Miandry (${pendingSellers.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Ireo mpivarotra vao nisoratra anarana mila fankatoavana",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        if (pendingSellers.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MalagasyGreen, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tsy misy mpivarotra miandry fankatoavana amin'izao.", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            items(pendingSellers) { seller ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(seller.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Imailaka: ${seller.email}", fontSize = 12.sp, color = Color.Gray)
                        Text("Tel: ${seller.phone}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("Faritra: ${seller.region} (${seller.city})", fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { onApproveSeller(seller.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_approve_seller_button_${seller.id}")
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("APPROVE", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onRejectSeller(seller.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_reject_seller_button_${seller.id}")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("REJECT", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductModerationTab(
    pendingProducts: List<ProductEntity>,
    onApproveProduct: (String) -> Unit,
    onRejectProduct: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Entana Miandry Fanamarinana (${pendingProducts.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Entana nampidirin'ny mpivarotra mila fankatoavana vao azo avoaka",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        if (pendingProducts.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MalagasyGreen, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tsy misy entana miandry fankatoavana amin'izao.", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            items(pendingProducts) { prod ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(prod.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(formatAriary(prod.price), fontWeight = FontWeight.ExtraBold, color = MalagasyGreen, fontSize = 14.sp)
                        Text("Sokajy: ${prod.category} • Faritra: ${prod.region}", fontSize = 12.sp, color = Color.Gray)
                        Text("Mpivarotra: ${prod.sellerName} (${prod.sellerPhone})", fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { onApproveProduct(prod.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_approve_product_button_${prod.id}")
                            ) {
                                Text("APPROVE", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onRejectProduct(prod.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_reject_product_button_${prod.id}")
                            ) {
                                Text("REJECT", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserManagementTab(allUsers: List<UserEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(allUsers) { user ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${user.email} • ${user.phone}", fontSize = 11.sp, color = Color.Gray)
                        Text("Toerana: ${user.region}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (user.accountType) {
                            "ADMIN" -> Color(0xFF6A1B9A)
                            "SELLER" -> MalagasyGreen
                            else -> Color.Gray
                        },
                        contentColor = Color.White
                    ) {
                        Text(
                            text = user.accountType,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportsManagementTab(allReports: List<ReportEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (allReports.isEmpty()) {
            item {
                Text("Tsy misy tatitra na fitarainana.", color = Color.Gray)
            }
        } else {
            items(allReports) { report ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Antony: ${report.reason}", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 14.sp)
                        Text("Details: ${report.description}", fontSize = 12.sp)
                        Text("Cible: ${report.targetType} (${report.targetId})", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRevenuesTab(transactions: List<MonetizationTransactionEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            MonetizationRevenueSummaryCard(transactions = transactions)
        }

        item {
            Text(
                text = "Tantara ny Fandoavam-bola (${transactions.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (transactions.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("Tsy mbola misy transaction nidirana amin'ny plateforme.", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        } else {
            items(transactions) { txn ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(txn.description, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Mpampiasa: ${txn.userName} (${txn.phone})", fontSize = 11.sp, color = Color.Gray)
                            Text("Mode: ${txn.paymentMethod} • Ref: ${txn.referenceCode}", fontSize = 10.sp, color = Color.DarkGray)
                        }

                        Text(
                            text = formatAriarySimple(txn.amount),
                            fontWeight = FontWeight.Black,
                            color = MalagasyGreen,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
