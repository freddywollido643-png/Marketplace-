package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserEntity
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@Composable
fun ProfileScreen(
    currentUser: UserEntity?,
    onSwitchAccountClick: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenSellerDashboard: () -> Unit,
    onOpenAuthScreen: () -> Unit,
    onOpenAboutTerms: () -> Unit,
    onContactWhatsApp: () -> Unit,
    onShowMonetizationGuide: () -> Unit = {},
    onUpgradeProClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Mombamomba (Profile)",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MalagasyGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (currentUser == null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tsy mbola tafiditra kaonty ianao", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenAuthScreen,
                        colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Miditra / Misoratra Anarana", color = Color.White)
                    }
                }
            }
        } else {
            // Profile Card Header
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(MalagasyGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser.fullName.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(currentUser.fullName, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                            Text(currentUser.email, fontSize = 12.sp, color = Color.Gray)
                            Text("Tel: ${currentUser.phone}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = when (currentUser.accountType) {
                                        "ADMIN" -> Color(0xFF6A1B9A)
                                        "SELLER" -> MalagasyGreen
                                        else -> Color.Gray
                                    },
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = currentUser.accountType,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                if (currentUser.isVerifiedSeller && currentUser.sellerStatus == "APPROVED") {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Mpivarotra voamarina ✅",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MalagasyGreen
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Faritra: ${currentUser.region}", fontSize = 12.sp, color = Color.DarkGray)
                        Text("Tanàna: ${currentUser.city}", fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Account Switcher Button (Crucial for live demo & role verification)
            OutlinedButton(
                onClick = onSwitchAccountClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MalagasyGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("switch_account_button")
            ) {
                Icon(Icons.Default.SwapHoriz, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Hanova kaonty andrana (Switch Account)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            Text("Safidy sy Fitantanana (Settings)", fontWeight = FontWeight.Bold, fontSize = 15.sp)

            Spacer(modifier = Modifier.height(8.dp))

            ProfileMenuItem(
                icon = Icons.Default.MonetizationOn,
                title = "Fomba Ahazoana Vola (Business Model)",
                subtitle = "Jereo amin'ny antsipiriany ny fomba fampidirana vola Alibaba",
                onClick = onShowMonetizationGuide
            )

            if (currentUser.accountType == "SELLER") {
                ProfileMenuItem(
                    icon = Icons.Default.WorkspacePremium,
                    title = "Lasa Mpivarotra Gold PRO (15 000 Ar)",
                    subtitle = "Badge Gold Verified, fampidirana entana vao mainka sy komisiona 2%",
                    onClick = onUpgradeProClick
                )
            }

            if (currentUser.accountType == "ADMIN") {
                ProfileMenuItem(
                    icon = Icons.Default.AdminPanelSettings,
                    title = "Tableau de Bord Administrateur",
                    subtitle = "Ankatoavy ny mpivarotra sy moderer-ko ny entana",
                    onClick = onOpenAdminDashboard
                )
            }

            ProfileMenuItem(
                icon = Icons.Default.Dashboard,
                title = "Tableau de Bord Mpivarotra",
                subtitle = "Jereo ny entanao sy ny antontan'isa",
                onClick = onOpenSellerDashboard
            )

            ProfileMenuItem(
                icon = Icons.Default.Help,
                title = "Mombamomba ny TSENA MALAGASY",
                subtitle = "À propos, Conditions d'utilisation sy règles mpivarotra",
                onClick = onOpenAboutTerms
            )

            ProfileMenuItem(
                icon = Icons.Default.Phone,
                title = "Mifandraisa amin'ny WhatsApp (+261 38 56 513 78)",
                subtitle = "Fifandraisana mivantana amin'ny administrateur",
                onClick = onContactWhatsApp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onOpenAuthScreen,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Hiala amin'ny kaonty (Logout)", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MalagasyGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}
