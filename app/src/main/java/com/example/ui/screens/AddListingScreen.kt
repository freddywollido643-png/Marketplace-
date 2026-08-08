package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.entity.UserEntity
import com.example.data.model.DEFAULT_CATEGORIES
import com.example.data.model.MADAGASCAR_REGIONS
import com.example.ui.components.SellerApprovalBanner
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen(
    currentUser: UserEntity?,
    onContactWhatsApp: () -> Unit,
    onSubmitListing: (
        title: String,
        description: String,
        priceStr: String,
        category: String,
        subcategory: String,
        condition: String,
        quantityStr: String,
        imageUrls: String,
        region: String,
        city: String,
        deliveryAvailable: Boolean,
        negotiationAvailable: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) -> Unit,
    onSuccessSubmitted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(DEFAULT_CATEGORIES.first().nameMg) }
    var subcategory by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Neuf") }
    var quantityStr by remember { mutableStateOf("1") }
    var imageUrls by remember { mutableStateOf("https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600") }
    var region by remember { mutableStateOf(currentUser?.region ?: MADAGASCAR_REGIONS.first()) }
    var city by remember { mutableStateOf(currentUser?.city ?: "Antananarivo Renivohitra") }
    var deliveryAvailable by remember { mutableStateOf(true) }
    var negotiationAvailable by remember { mutableStateOf(true) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var regionExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Hivarotra Entana (Add Listing)",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MalagasyGreen
        )
        Text(
            text = "Ampidiro eto ny mombamomba ny entana tianao hamidy",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Check if seller is approved or pending
        if (currentUser == null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mila miditra kaonty na misoratra anarana ianao hivarotra entana.",
                    modifier = Modifier.padding(16.dp)
                )
            }
            return
        }

        if (currentUser.accountType == "SELLER" && currentUser.sellerStatus != "APPROVED") {
            SellerApprovalBanner(
                currentUser = currentUser,
                onContactWhatsApp = onContactWhatsApp
            )
            return
        }

        if (currentUser.accountType == "BUYER") {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Fankatoavana mpivarotra ilaina",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ianao dia manana kaonty Mpividy (Buyer). Mba hahafahanao mivarotra ao amin'ny TSENA MALAGASY dia mila mangataka kaonty Mpivarotra ianao.",
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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

        // Form Fields
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Anaran'ny entana (Title) *") },
            placeholder = { Text("Ohatra: iPhone 13 Pro 256GB Gold") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_product_title_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Dropdown
        Text("Sokajy (Category) *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("add_product_category_dropdown")
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                DEFAULT_CATEGORIES.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.nameMg) },
                        onClick = {
                            category = cat.nameMg
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = priceStr,
            onValueChange = { priceStr = it },
            label = { Text("Vidy amin'ny Ariary (Price in Ar) *") },
            placeholder = { Text("Ohatra: 2500000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_product_price_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Satan'ny entana (Condition) *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(
                selected = condition == "Neuf",
                onClick = { condition = "Neuf" },
                label = { Text("Neuf (Vao)") }
            )
            FilterChip(
                selected = condition == "Occasion",
                onClick = { condition = "Occasion" },
                label = { Text("Occasion (Efa niasa)") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Mpanazava momba ny entana (Description) *") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_product_description_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Kaominina / Tanàna (City/Commune) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = imageUrls,
            onValueChange = { imageUrls = it },
            label = { Text("Sarin'ny entana URL (Image URL)") },
            placeholder = { Text("https://...") },
            modifier = Modifier.fillMaxWidth()
        )

        if (imageUrls.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                AsyncImage(
                    model = imageUrls,
                    contentDescription = "Sary asehona",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = deliveryAvailable,
                onCheckedChange = { deliveryAvailable = it }
            )
            Text("Afaka aterina (Delivery Available)", fontSize = 13.sp)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = negotiationAvailable,
                onCheckedChange = { negotiationAvailable = it }
            )
            Text("Afaka miady varotra (Negotiable Price)", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                errorMessage = null
                if (title.isBlank() || priceStr.isBlank() || description.isBlank()) {
                    errorMessage = "Fenoy daholo ny sahan'asa ilaina (*)"
                    return@Button
                }
                onSubmitListing(
                    title, description, priceStr, category, subcategory, condition,
                    quantityStr, imageUrls, region, city, deliveryAvailable, negotiationAvailable,
                    {
                        onSuccessSubmitted()
                    },
                    { err ->
                        errorMessage = err
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MalagasyGreen),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_product_button")
        ) {
            Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hamoaka ny entana (Submit)",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Rehefa alefa dia ho: PENDING_REVIEW (Miandry fanamarinana ataon'ny administrateur).",
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}
