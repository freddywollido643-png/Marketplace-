package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.MonetizationTransactionEntity
import com.example.data.entity.ProductEntity
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonetizationGuideDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight(0.85f)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MalagasyGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = MalagasyGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Ahona no ahazoana Vola?",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = MalagasyGreen
                            )
                            Text(
                                text = "Modèle Économique Alibaba Madagascar",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Default.Close, contentDescription = "Akatona")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        MonetizationModelCard(
                            stepNumber = "1",
                            title = "Komisiona amin'ny Varotra (3% - 5%)",
                            subtitle = "Frais de transaction amin'ny Mobile Money",
                            description = "Isaky ny misy varotra vita na commande lasa amin'ny alalan'ny plateforme dia misy komisiona 3% hatramin'ny 5% alain'ny plateforme amin'ny mpivarotra (mandoa amin'ny MVola, Orange Money, Airtel Money).",
                            badgeText = "Auto Fee 3-5%",
                            badgeColor = Color(0xFF10B981)
                        )
                    }

                    item {
                        MonetizationModelCard(
                            stepNumber = "2",
                            title = "Boost Entana / Sponsorisé Gold",
                            subtitle = "Loha-laharana amin'ny Pejy Fandraisana",
                            description = "Ny mpivarotra dia afaka mandoa 2,000 Ar (3 andro), 5,000 Ar (7 andro) na 15,000 Ar (30 andro) mba ho ambony indrindra sy hisongadina 'SPONSORISÉ GOLD' eo amin'ny pejy fandraisana sy karoka.",
                            badgeText = "2 000 Ar - 15 000 Ar",
                            badgeColor = MalagasyGold
                        )
                    }

                    item {
                        MonetizationModelCard(
                            stepNumber = "3",
                            title = "Abonnement Konty PRO (Gold Supplier)",
                            subtitle = "Konto Mpivarotra Lehibe Voamarina",
                            description = "Ny mpivarotra matanjaka, ozinina na mpanafatra entana dia mandoa abonnement 15,000 Ar/Volana hahazoana Badge PRO Supplier Gold, fampidirana entana tsy voafetra ary komisiona ahena ho 2%.",
                            badgeText = "15 000 Ar / Volana",
                            badgeColor = Color(0xFF3B82F6)
                        )
                    }

                    item {
                        MonetizationModelCard(
                            stepNumber = "4",
                            title = "Banners Dokam-barotra (Pubs)",
                            subtitle = "Espaces Publicitaires ho an'ny Orinasa",
                            description = "Espaces banners eo amin'ny pejy fandraisana ho an'ny orinasa, banky, fifandraisana sy ozinina te hanao dokam-barotra miaraka amin'ny TSENA MALAGASY.",
                            badgeText = "Banners Pub",
                            badgeColor = Color(0xFF8B5CF6)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Azoko tsara, Misaotra!", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MonetizationModelCard(
    stepNumber: String,
    title: String,
    subtitle: String,
    description: String,
    badgeText: String,
    badgeColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MalagasyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepNumber,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor,
                    contentColor = Color.White
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MalagasyGreen
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 11.sp,
                color = Color(0xFF475569),
                lineHeight = 15.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoostProductDialog(
    product: ProductEntity,
    onDismissRequest: () -> Unit,
    onConfirmBoost: (amount: Long, paymentMethod: String, phone: String, refCode: String) -> Unit
) {
    var selectedPlan by remember { mutableStateOf("7_DAYS") } // 3_DAYS, 7_DAYS, 30_DAYS
    var selectedOperator by remember { mutableStateOf("MVOLA") } // MVOLA, ORANGE_MONEY, AIRTEL_MONEY
    var phone by remember { mutableStateOf("0341234567") }
    var refCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val amount = when (selectedPlan) {
        "3_DAYS" -> 2000L
        "7_DAYS" -> 5000L
        else -> 15000L
    }

    val merchantNumber = when (selectedOperator) {
        "MVOLA" -> "+261 34 88 990 00 (TSENA MVola)"
        "ORANGE_MONEY" -> "+261 32 88 990 00 (TSENA Orange)"
        else -> "+261 33 88 990 00 (TSENA Airtel)"
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = MalagasyGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Boost Entana Gold", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MalagasyGreen)
                    }
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Default.Close, contentDescription = "Akatona")
                    }
                }

                Text(
                    text = "Lasa ambony indrindra amin'ny pejy fandraisana ny entana '${product.title}'",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Plan selection
                Text("Misafidiana faharetana:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PlanChip(
                        title = "3 Andro",
                        price = "2 000 Ar",
                        isSelected = selectedPlan == "3_DAYS",
                        onClick = { selectedPlan = "3_DAYS" },
                        modifier = Modifier.weight(1f)
                    )
                    PlanChip(
                        title = "7 Andro (VIP)",
                        price = "5 000 Ar",
                        isSelected = selectedPlan == "7_DAYS",
                        onClick = { selectedPlan = "7_DAYS" },
                        modifier = Modifier.weight(1f)
                    )
                    PlanChip(
                        title = "1 Volana",
                        price = "15 000 Ar",
                        isSelected = selectedPlan == "30_DAYS",
                        onClick = { selectedPlan = "30_DAYS" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Payment operator selection
                Text("Mandoa amin'ny Mobile Money:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OperatorChip("MVola", selectedOperator == "MVOLA", { selectedOperator = "MVOLA" }, Modifier.weight(1f))
                    OperatorChip("Orange", selectedOperator == "ORANGE_MONEY", { selectedOperator = "ORANGE_MONEY" }, Modifier.weight(1f))
                    OperatorChip("Airtel", selectedOperator == "AIRTEL_MONEY", { selectedOperator = "AIRTEL_MONEY" }, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Laharan'ny Plateforme handefasana $amount Ar:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        Text(merchantNumber, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFB45309))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Laharan'ny findainao (Payer)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = refCode,
                    onValueChange = { refCode = it },
                    label = { Text("Référence de Transaction Mobile Money") },
                    placeholder = { Text("Oh: TXN98765432") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(errorMessage!!, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (refCode.isBlank()) {
                            refCode = "TXN_${System.currentTimeMillis().toString().takeLast(6)}"
                        }
                        onConfirmBoost(amount, selectedOperator, phone, refCode)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MalagasyGold, contentColor = Color.Black),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("confirm_boost_button")
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Andao Hanamafy & Handoa $amount Ar", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun PlanChip(
    title: String,
    price: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFEF3C7) else Color(0xFFF1F5F9)
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) MalagasyGold else Color(0xFFE2E8F0)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = if (isSelected) Color(0xFF92400E) else Color.DarkGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = price,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected) MalagasyGreen else Color.Black
            )
        }
    }
}

@Composable
fun OperatorChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MalagasyGreen else Color(0xFFF1F5F9)
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.DarkGray
            )
        }
    }
}

@Composable
fun MonetizationRevenueSummaryCard(
    transactions: List<MonetizationTransactionEntity>,
    modifier: Modifier = Modifier
) {
    val totalRevenue = transactions.sumOf { it.amount }
    val commissionRevenue = transactions.filter { it.type == "COMMISSION" }.sumOf { it.amount }
    val boostRevenue = transactions.filter { it.type == "PRODUCT_BOOST" }.sumOf { it.amount }
    val proRevenue = transactions.filter { it.type == "PRO_MEMBERSHIP" }.sumOf { it.amount }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MalagasyGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = null, tint = MalagasyGreen)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Vola Miditra Rehetra (Revenus)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Kajy Plateforme TSENA MALAGASY", fontSize = 10.sp, color = Color.Gray)
                    }
                }

                Text(
                    text = "${formatAriarySimple(totalRevenue)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = MalagasyGreen
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Divider(color = Color(0xFFF1F5F9))

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RevenueMiniStat("Komisiona 3-5%", formatAriarySimple(commissionRevenue), Modifier.weight(1f))
                RevenueMiniStat("Boost Entana", formatAriarySimple(boostRevenue), Modifier.weight(1f))
                RevenueMiniStat("Konty PRO", formatAriarySimple(proRevenue), Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun RevenueMiniStat(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .padding(8.dp)
    ) {
        Text(title, fontSize = 10.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MalagasyGreen)
    }
}

fun formatAriarySimple(amount: Long): String {
    val formatter = java.text.NumberFormat.getInstance(java.util.Locale.FRANCE)
    return "${formatter.format(amount)} Ar"
}
