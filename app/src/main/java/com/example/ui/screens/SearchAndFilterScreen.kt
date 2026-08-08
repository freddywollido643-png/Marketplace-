package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.model.DEFAULT_CATEGORIES
import com.example.data.model.MADAGASCAR_REGIONS
import com.example.ui.components.ProductCardItem
import com.example.ui.theme.MalagasyGold
import com.example.ui.theme.MalagasyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    selectedCategory: String?,
    onCategoryChange: (String?) -> Unit,
    selectedRegion: String?,
    onRegionChange: (String?) -> Unit,
    selectedCondition: String?,
    onConditionChange: (String?) -> Unit,
    minPrice: Long?,
    onMinPriceChange: (Long?) -> Unit,
    maxPrice: Long?,
    onMaxPriceChange: (Long?) -> Unit,
    sortBy: String?,
    onSortByChange: (String?) -> Unit,
    products: List<ProductEntity>,
    favoriteProductIds: Set<String>,
    onProductClick: (ProductEntity) -> Unit,
    onFavoriteClick: (ProductEntity) -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterPanel by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var regionExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        // Search Header Bar
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    placeholder = { Text("Inona no tadiavinao ? (Search...)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MalagasyGreen) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Fafao")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MalagasyGreen,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input_field")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = showFilterPanel,
                        onClick = { showFilterPanel = !showFilterPanel },
                        leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        label = { Text(if (showFilterPanel) "Akatona ny filtres" else "Filtres avançés", fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(14.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = showFilterPanel,
                            borderColor = Color(0xFFE5E7EB),
                            selectedBorderColor = MalagasyGreen
                        ),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFF1F5F9),
                            selectedContainerColor = MalagasyGreen,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("toggle_filters_button")
                    )

                    TextButton(onClick = onResetFilters) {
                        Text("Averina daholo", fontSize = 12.sp, color = MalagasyGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Filter Panel
        AnimatedVisibility(visible = showFilterPanel) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sokajy (Category)", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    // Category Dropdown Box
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory ?: "Sokajy rehetra",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sokajy rehetra") },
                                onClick = {
                                    onCategoryChange(null)
                                    categoryExpanded = false
                                }
                            )
                            DEFAULT_CATEGORIES.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.nameMg) },
                                    onClick = {
                                        onCategoryChange(cat.nameMg)
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Faritra (Region)", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    // Region Dropdown Box
                    ExposedDropdownMenuBox(
                        expanded = regionExpanded,
                        onExpandedChange = { regionExpanded = !regionExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedRegion ?: "Faritra rehetra",
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
                            DropdownMenuItem(
                                text = { Text("Faritra rehetra") },
                                onClick = {
                                    onRegionChange(null)
                                    regionExpanded = false
                                }
                            )
                            MADAGASCAR_REGIONS.forEach { reg ->
                                DropdownMenuItem(
                                    text = { Text(reg) },
                                    onClick = {
                                        onRegionChange(reg)
                                        regionExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Satan'ny entana (Condition)", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedCondition == null,
                            onClick = { onConditionChange(null) },
                            label = { Text("Rehetra") }
                        )
                        FilterChip(
                            selected = selectedCondition == "Neuf",
                            onClick = { onConditionChange("Neuf") },
                            label = { Text("Neuf") }
                        )
                        FilterChip(
                            selected = selectedCondition == "Occasion",
                            onClick = { onConditionChange("Occasion") },
                            label = { Text("Occasion") }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Laharana filaharana (Sort)", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = sortBy == "newest",
                            onClick = { onSortByChange("newest") },
                            label = { Text("Vaovao indrindra") }
                        )
                        FilterChip(
                            selected = sortBy == "price_low",
                            onClick = { onSortByChange("price_low") },
                            label = { Text("Mora indrindra") }
                        )
                        FilterChip(
                            selected = sortBy == "price_high",
                            onClick = { onSortByChange("price_high") },
                            label = { Text("Lafo indrindra") }
                        )
                    }
                }
            }
        }

        // Search Results List
        if (products.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tsy nahitana vokatra mifanaraka amin'ny fikarohanao.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Andramo ovaiana ny teny na sokajy filtres.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Vokatra hita (${products.size}):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                items(products.chunked(2)) { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
        }
    }
}
