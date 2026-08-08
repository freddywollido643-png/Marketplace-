package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.DatabaseSeedData
import com.example.data.entity.UserEntity
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen
import com.example.ui.theme.MalagasyGreenDark

enum class MainTab(val titleMg: String, val route: String) {
    HOME("Fandraisana", "home"),
    SEARCH("Fikarohana", "search"),
    SELL("Hivarotra", "sell"),
    MESSAGES("Hafatra", "messages"),
    PROFILE("Profil", "profile")
}

@Composable
fun MarketplaceBottomBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    unreadMessageCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 12.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                val isSelected = currentTab == tab

                if (tab == MainTab.SELL) {
                    // Central Bento Floating Gold FAB
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(y = (-8).dp)
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MalagasyGold)
                            .border(3.dp, Color(0xFFF3F4F6), CircleShape)
                            .clickable { onTabSelected(tab) }
                            .testTag("nav_tab_${tab.route}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = tab.titleMg,
                            tint = MalagasyGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("nav_tab_${tab.route}")
                    ) {
                        BadgedBox(badge = {
                            if (tab == MainTab.MESSAGES && unreadMessageCount > 0) {
                                Badge(containerColor = MalagasyGold, contentColor = Color.Black) {
                                    Text(unreadMessageCount.toString())
                                }
                            }
                        }) {
                            Icon(
                                imageVector = when (tab) {
                                    MainTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                                    MainTab.SEARCH -> if (isSelected) Icons.Filled.Search else Icons.Outlined.Search
                                    MainTab.SELL -> Icons.Filled.Add
                                    MainTab.MESSAGES -> if (isSelected) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline
                                    MainTab.PROFILE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.PersonOutline
                                },
                                contentDescription = tab.titleMg,
                                tint = if (isSelected) MalagasyGreen else Color(0xFF94A3B8),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = tab.titleMg,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) MalagasyGreen else Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountSwitcherDialog(
    currentUser: UserEntity?,
    onUserSelected: (UserEntity) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Fidio ny kaontinao (Switch Account)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MalagasyGreen
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Fidio ny kaonty tianao nampiasaina mba hanandramana ny satan'ny Mpividy, Mpivarotra na Administrateur:",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val accounts = listOf(
                    DatabaseSeedData.DEFAULT_BUYER,
                    DatabaseSeedData.APPROVED_SELLER_1,
                    DatabaseSeedData.APPROVED_SELLER_2,
                    DatabaseSeedData.PENDING_SELLER,
                    DatabaseSeedData.ADMIN_USER
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(accounts) { acc ->
                        val isSelected = currentUser?.id == acc.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF1F5F9)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUserSelected(acc)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = acc.fullName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${acc.accountType} • ${acc.region}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = when (acc.sellerStatus) {
                                        "APPROVED" -> MalagasyGreen
                                        "PENDING_APPROVAL" -> MalagasyGold
                                        else -> Color.Gray
                                    },
                                    contentColor = if (acc.sellerStatus == "PENDING_APPROVAL") Color.Black else Color.White
                                ) {
                                    Text(
                                        text = if (acc.accountType == "ADMIN") "ADMIN" else acc.sellerStatus,
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
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Akatona (Close)")
            }
        }
    )
}

@Composable
fun ReportProductDialog(
    targetTitle: String,
    onSubmit: (reason: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedReason by remember { mutableStateOf("fraude") }
    var descriptionText by remember { mutableStateOf("") }

    val reportReasons = listOf(
        "fraude" to "Fraude / Halatra",
        "faux produit" to "Faux produit / Entana hosoka",
        "prix trompeur" to "Prix trompeur / Vidy mamitaka",
        "contenu interdit" to "Contenu interdit / Voarara",
        "comportement suspect" to "Comportement suspect / Zava-mampiahiahy",
        "autre" to "Autre / Hafa"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Signaler : $targetTitle", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Antony ilazana fitarainana (Reason):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(8.dp))

                reportReasons.forEach { (code, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = code }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedReason == code,
                            onClick = { selectedReason = code }
                        )
                        Text(text = label, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text("Mpanazava fanampiny (Details)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(selectedReason, descriptionText)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Alefa ny fitarainana", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Akanjo (Cancel)")
            }
        }
    )
}

@Composable
fun OrderAndMobilePaymentModal(
    productTitle: String,
    productPrice: Long,
    sellerName: String,
    onConfirmOrder: (address: String, paymentMethod: String) -> Unit,
    onDismiss: () -> Unit
) {
    var shippingAddress by remember { mutableStateOf("Antananarivo Renivohitra, Analamanga") }
    var selectedMethod by remember { mutableStateOf("MVOLA") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Hividy entana (Buy Product)", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MalagasyGreen)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = productTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Vidiny: ${formatAriary(productPrice)}", fontWeight = FontWeight.ExtraBold, color = MalagasyGreen, fontSize = 16.sp)
                Text(text = "Mpivarotra: $sellerName", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = shippingAddress,
                    onValueChange = { shippingAddress = it },
                    label = { Text("Adiresy handefasana (Delivery Address)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Fomba fandoavam-bola (Payment Method):", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                val methods = listOf(
                    "MVOLA" to "MVola (Telma)",
                    "ORANGE_MONEY" to "Orange Money",
                    "AIRTEL_MONEY" to "Airtel Money",
                    "CASH" to "Paiement à la livraison (Keshina)"
                )

                methods.forEach { (code, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = code }
                    ) {
                        RadioButton(selected = selectedMethod == code, onClick = { selectedMethod = code })
                        Text(text = label, fontSize = 13.sp)
                    }
                }

                if (selectedMethod != "CASH") {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFE65100))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Paiement mobile bientôt disponible. Handefa commande sy fifampiraharahana mivantana amin'ny mpivarotra ianao.",
                                fontSize = 11.sp,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmOrder(shippingAddress, selectedMethod)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen)
            ) {
                Text("Avereno ny komandy (Place Order)", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Akatona")
            }
        }
    )
}
