package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.entity.ProductEntity
import com.example.ui.components.AccountSwitcherDialog
import com.example.ui.components.BoostProductDialog
import com.example.ui.components.MainTab
import com.example.ui.components.MarketplaceBottomBar
import com.example.ui.components.MonetizationGuideDialog
import com.example.ui.components.OfflineNotificationBanner
import com.example.ui.components.TopBarHeader
import com.example.ui.screens.*
import com.example.ui.theme.TsenaMalagasyTheme
import com.example.ui.viewmodel.MarketplaceViewModel
import com.example.util.NetworkObserver

class MainActivity : ComponentActivity() {

    private val viewModel: MarketplaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TsenaMalagasyTheme {
                val networkObserver = remember { NetworkObserver(applicationContext) }
                val isConnected by networkObserver.isConnected.collectAsStateWithLifecycle(initialValue = true)

                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                val approvedProducts by viewModel.searchResults.collectAsStateWithLifecycle()
                val userFavorites by viewModel.userFavorites.collectAsStateWithLifecycle()
                val sellerProducts by viewModel.sellerProducts.collectAsStateWithLifecycle()
                val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()

                val pendingSellers by viewModel.pendingSellers.collectAsStateWithLifecycle()
                val pendingProducts by viewModel.pendingProducts.collectAsStateWithLifecycle()
                val allUsersAdmin by viewModel.allUsersAdmin.collectAsStateWithLifecycle()
                val allProductsAdmin by viewModel.allProductsAdmin.collectAsStateWithLifecycle()
                val allReportsAdmin by viewModel.allReportsAdmin.collectAsStateWithLifecycle()
                val monetizationTransactions by viewModel.allMonetizationTransactions.collectAsStateWithLifecycle()

                val userConversations by viewModel.userConversations.collectAsStateWithLifecycle()
                val userNotifications by viewModel.userNotifications.collectAsStateWithLifecycle()

                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                val selectedRegion by viewModel.selectedRegion.collectAsStateWithLifecycle()
                val selectedCondition by viewModel.selectedCondition.collectAsStateWithLifecycle()
                val minPrice by viewModel.minPrice.collectAsStateWithLifecycle()
                val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
                val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()

                val favoriteIds = remember(userFavorites) { userFavorites.map { it.productId }.toSet() }
                val unreadNotifications = remember(userNotifications) { userNotifications.count { !it.isRead } }

                var currentTab by remember { mutableStateOf(MainTab.HOME) }
                var secondaryScreen by remember { mutableStateOf<String?>(null) } // "DETAIL", "ADMIN", "SELLER", "AUTH", "ABOUT"
                var showAccountSwitcher by remember { mutableStateOf(false) }
                var showMonetizationGuide by remember { mutableStateOf(false) }
                var productToBoost by remember { mutableStateOf<ProductEntity?>(null) }

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    viewModel.uiEvents.collect { msg ->
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                LaunchedEffect(isConnected) {
                    if (!isConnected) {
                        Toast.makeText(
                            this@MainActivity,
                            "Fampandrenesana: Tsy manokatra Data na Wi-Fi ianao. Tsy misy Internet!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                val openWhatsApp = {
                    val whatsappUrl = "https://api.whatsapp.com/send?phone=261385651378&text=Salama%20administrateur%20TSENA%20MALAGASY"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                    startActivity(intent)
                }

                Scaffold(
                    topBar = {
                        TopBarHeader(
                            currentUser = currentUser,
                            unreadNotificationCount = unreadNotifications,
                            unreadMessageCount = userConversations.size,
                            onSearchClick = {
                                secondaryScreen = null
                                currentTab = MainTab.SEARCH
                            },
                            onNotificationsClick = {
                                viewModel.showToast("Fampandrenesana: Manana fampandrenesana $unreadNotifications ianao")
                            },
                            onMessagesClick = {
                                secondaryScreen = null
                                currentTab = MainTab.MESSAGES
                            },
                            onProfileClick = {
                                secondaryScreen = null
                                currentTab = MainTab.PROFILE
                            },
                            onAccountSwitchClick = {
                                showAccountSwitcher = true
                            }
                        )
                    },
                    bottomBar = {
                        if (secondaryScreen == null) {
                            MarketplaceBottomBar(
                                currentTab = currentTab,
                                onTabSelected = { tab -> currentTab = tab },
                                unreadMessageCount = userConversations.size
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        // Offline Network Banner
                        OfflineNotificationBanner(
                            isOffline = !isConnected,
                            onOpenSettingsClick = {
                                try {
                                    val intent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                                    startActivity(intent)
                                } catch (_: Exception) {
                                    Toast.makeText(this@MainActivity, "Ampaherezo ny Data na Wi-Fi amin'ny Paramètres", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        Box(modifier = Modifier.weight(1f)) {
                            AnimatedContent(
                            targetState = secondaryScreen ?: currentTab.route,
                            label = "MainNavigation"
                        ) { target ->
                            when (target) {
                                "home" -> HomeScreen(
                                    currentUser = currentUser,
                                    approvedProducts = approvedProducts,
                                    favoriteProductIds = favoriteIds,
                                    onProductClick = { prod ->
                                        viewModel.selectProduct(prod)
                                        secondaryScreen = "DETAIL"
                                    },
                                    onFavoriteClick = { prod ->
                                        viewModel.toggleFavorite(prod.id)
                                    },
                                    onSearchClick = { currentTab = MainTab.SEARCH },
                                    onCategoryClick = { cat ->
                                        viewModel.selectedCategory.value = cat
                                        currentTab = MainTab.SEARCH
                                    },
                                    onSellClick = { currentTab = MainTab.SELL },
                                    onContactWhatsApp = openWhatsApp
                                )

                                "search" -> SearchAndFilterScreen(
                                    searchQuery = searchQuery,
                                    onQueryChange = { viewModel.searchQuery.value = it },
                                    selectedCategory = selectedCategory,
                                    onCategoryChange = { viewModel.selectedCategory.value = it },
                                    selectedRegion = selectedRegion,
                                    onRegionChange = { viewModel.selectedRegion.value = it },
                                    selectedCondition = selectedCondition,
                                    onConditionChange = { viewModel.selectedCondition.value = it },
                                    minPrice = minPrice,
                                    onMinPriceChange = { viewModel.minPrice.value = it },
                                    maxPrice = maxPrice,
                                    onMaxPriceChange = { viewModel.maxPrice.value = it },
                                    sortBy = sortBy,
                                    onSortByChange = { viewModel.sortBy.value = it },
                                    products = approvedProducts,
                                    favoriteProductIds = favoriteIds,
                                    onProductClick = { prod ->
                                        viewModel.selectProduct(prod)
                                        secondaryScreen = "DETAIL"
                                    },
                                    onFavoriteClick = { prod ->
                                        viewModel.toggleFavorite(prod.id)
                                    },
                                    onResetFilters = { viewModel.resetFilters() }
                                )

                                "sell" -> AddListingScreen(
                                    currentUser = currentUser,
                                    onContactWhatsApp = openWhatsApp,
                                    onSubmitListing = { title, desc, priceStr, cat, subcat, cond, qtyStr, imgs, reg, city, deliv, nego, onSuccess, onError ->
                                        viewModel.createListing(
                                            title, desc, priceStr, cat, subcat, cond, qtyStr, imgs, reg, city, deliv, nego, onSuccess, onError
                                        )
                                    },
                                    onSuccessSubmitted = {
                                        currentTab = MainTab.HOME
                                    }
                                )

                                "messages" -> MessagesScreen(
                                    currentUser = currentUser,
                                    conversations = userConversations,
                                    messagesFlow = { convId -> viewModel.repository.getMessages(convId) },
                                    onSendMessage = { convId, receiverId, text ->
                                        viewModel.sendMessage(convId, receiverId, text)
                                    }
                                )

                                "profile" -> ProfileScreen(
                                    currentUser = currentUser,
                                    onSwitchAccountClick = { showAccountSwitcher = true },
                                    onOpenAdminDashboard = { secondaryScreen = "ADMIN" },
                                    onOpenSellerDashboard = { secondaryScreen = "SELLER" },
                                    onOpenAuthScreen = { secondaryScreen = "AUTH" },
                                    onOpenAboutTerms = { secondaryScreen = "ABOUT" },
                                    onContactWhatsApp = openWhatsApp,
                                    onShowMonetizationGuide = { showMonetizationGuide = true },
                                    onUpgradeProClick = {
                                        viewModel.upgradeToPro(15000L, "MVOLA", currentUser?.phone ?: "0340000000", "PRO_MVOLA_888") {
                                            secondaryScreen = "SELLER"
                                        }
                                    }
                                )

                                "DETAIL" -> {
                                    if (selectedProduct != null) {
                                        ProductDetailScreen(
                                            product = selectedProduct!!,
                                            isFavorite = favoriteIds.contains(selectedProduct!!.id),
                                            onFavoriteClick = { viewModel.toggleFavorite(selectedProduct!!.id) },
                                            onContactSeller = { sId, sName, pId, pTitle, price, img ->
                                                viewModel.startConversation(sId, sName, pId, pTitle, price, img) { convId ->
                                                    secondaryScreen = null
                                                    currentTab = MainTab.MESSAGES
                                                }
                                            },
                                            onSubmitReport = { reason, details ->
                                                viewModel.submitReport("PRODUCT", selectedProduct!!.id, reason, details)
                                            },
                                            onBackClick = { secondaryScreen = null }
                                        )
                                    }
                                }

                                "ADMIN" -> AdminDashboardScreen(
                                    pendingSellers = pendingSellers,
                                    pendingProducts = pendingProducts,
                                    allUsers = allUsersAdmin,
                                    allProducts = allProductsAdmin,
                                    allReports = allReportsAdmin,
                                    monetizationTransactions = monetizationTransactions,
                                    onApproveSeller = { id -> viewModel.approveSeller(id) },
                                    onRejectSeller = { id -> viewModel.rejectSeller(id) },
                                    onApproveProduct = { id -> viewModel.approveProduct(id) },
                                    onRejectProduct = { id -> viewModel.rejectProduct(id) }
                                )

                                "SELLER" -> SellerDashboardScreen(
                                    currentUser = currentUser,
                                    sellerProducts = sellerProducts,
                                    onAddProductClick = {
                                        secondaryScreen = null
                                        currentTab = MainTab.SELL
                                    },
                                    onMarkAsSold = { id -> viewModel.markProductSold(id) },
                                    onDeleteProduct = { id -> viewModel.deleteProduct(id) },
                                    onContactWhatsApp = openWhatsApp,
                                    onBoostProductClick = { prod -> productToBoost = prod },
                                    onShowMonetizationGuide = { showMonetizationGuide = true }
                                )

                                "AUTH" -> AuthScreen(
                                    onLoginSubmit = { email, onSuccess, onError ->
                                        viewModel.login(email, onSuccess, onError)
                                    },
                                    onRegisterSubmit = { name, email, phone, reg, city, type, onSuccess ->
                                        viewModel.register(name, email, phone, reg, city, type, onSuccess)
                                    },
                                    onAuthSuccess = { secondaryScreen = null }
                                )

                                "ABOUT" -> AboutAndTermsScreen(
                                    onBackClick = { secondaryScreen = null }
                                )
                            }
                        }
                    }
                }
            }

            if (showAccountSwitcher) {
                AccountSwitcherDialog(
                    currentUser = currentUser,
                    onUserSelected = { selectedUser ->
                        viewModel.switchUserAccount(selectedUser)
                    },
                    onDismiss = { showAccountSwitcher = false }
                )
            }

            if (showMonetizationGuide) {
                MonetizationGuideDialog(
                    onDismissRequest = { showMonetizationGuide = false }
                )
            }

            if (productToBoost != null) {
                BoostProductDialog(
                    product = productToBoost!!,
                    onDismissRequest = { productToBoost = null },
                    onConfirmBoost = { amount, method, phone, ref ->
                        viewModel.boostProduct(productToBoost!!.id, amount, method, phone, ref) {
                            productToBoost = null
                        }
                    }
                )
            }
            }
        }
    }
}
