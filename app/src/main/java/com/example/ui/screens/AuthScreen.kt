package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MADAGASCAR_REGIONS
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginSubmit: (email: String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit,
    onRegisterSubmit: (
        fullName: String,
        email: String,
        phone: String,
        region: String,
        city: String,
        accountType: String,
        onSuccess: () -> Unit
    ) -> Unit,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var region by remember { mutableStateOf(MADAGASCAR_REGIONS.first()) }
    var city by remember { mutableStateOf("Antananarivo Renivohitra") }
    var accountType by remember { mutableStateOf("BUYER") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var regionExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .padding(bottom = 80.dp)
    ) {
        // App Header Branding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MalagasyGold
            ) {
                Text(
                    text = "TSENA MALAGASY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isRegisterMode) "Hamorona Kaonty Vaovao (Register)" else "Miditra amin'ny Kaonty (Login)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = MalagasyGreen
            )
        }

        if (errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (isRegisterMode) {
            // Account Type Selector (BUYER vs SELLER)
            Text("Karazana kaonty (Account Type) *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = accountType == "BUYER",
                    onClick = { accountType = "BUYER" },
                    label = { Text("Mpividy (Buyer)") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = accountType == "SELLER",
                    onClick = { accountType = "SELLER" },
                    label = { Text("Mpivarotra (Seller)") },
                    modifier = Modifier.weight(1f)
                )
            }

            if (accountType == "SELLER") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Mila fankatoavana avy amin'ny administrateur amin'ny WhatsApp (+261 38 56 513 78) ny kaonty Mpivarotra vao afaka mamoaka entana.",
                        fontSize = 11.sp,
                        color = Color(0xFFD84315),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Anarana sy Fanampiny (Full Name) *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_fullname_input")
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Adiresy Imailaka (Email) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_email_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (isRegisterMode) {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Laharana Telefaoina (Phone) *") },
                placeholder = { Text("034 00 000 00 / +261...") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_phone_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Region Dropdown
            Text("Faritra (Region) *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            ExposedDropdownMenuBox(
                expanded = regionExpanded,
                onExpandedChange = { regionExpanded = !regionExpanded }
            ) {
                OutlinedTextField(
                    value = region,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    MADAGASCAR_REGIONS.forEach { reg ->
                        DropdownMenuItem(
                            text = { Text(reg) },
                            onClick = {
                                region = reg
                                regionExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Tanàna / Kaominina (City) *") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Teny Mierina (Password) *") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_password_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                errorMessage = null
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Fenoy daholo ny imailaka sy teny mierina"
                    return@Button
                }

                if (isRegisterMode) {
                    if (fullName.isBlank() || phone.isBlank()) {
                        errorMessage = "Fenoy ny anarana sy ny laharana telefona"
                        return@Button
                    }
                    onRegisterSubmit(fullName, email, phone, region, city, accountType) {
                        onAuthSuccess()
                    }
                } else {
                    onLoginSubmit(email, {
                        onAuthSuccess()
                    }, { err ->
                        errorMessage = err
                    })
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_submit_button")
        ) {
            Text(
                text = if (isRegisterMode) "Misoratra Anarana" else "Miditra (Login)",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                isRegisterMode = !isRegisterMode
                errorMessage = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isRegisterMode) "Efa manana kaonty? Miditra eto" else "Tsy mbola manana kaonty? Hamorona kaonty eto",
                color = MalagasyGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
