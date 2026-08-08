package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DatabaseSeedData
import com.example.data.database.TsenaDatabase
import com.example.data.entity.ConversationEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.MessageEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReportEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.UserEntity
import com.example.data.repository.TsenaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MarketplaceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = TsenaDatabase.getInstance(application)
    val repository = TsenaRepository(database)

    // Current User
    val currentUser: StateFlow<UserEntity?> = repository.currentUser

    // Search and Filter States
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<String?>(null)
    val selectedRegion = MutableStateFlow<String?>(null)
    val selectedCondition = MutableStateFlow<String?>(null)
    val minPrice = MutableStateFlow<Long?>(null)
    val maxPrice = MutableStateFlow<Long?>(null)
    val sortBy = MutableStateFlow<String?>("newest")

    // UI Toast Messages
    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents: SharedFlow<String> = _uiEvents.asSharedFlow()

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiEvents.emit(message)
        }
    }

    // Active Search Products Stream
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<ProductEntity>> = combine(
        searchQuery, selectedCategory, selectedRegion, selectedCondition, minPrice, maxPrice, sortBy
    ) { flows: Array<Any?> ->
        TupleFilter(
            q = flows[0] as String?,
            cat = flows[1] as String?,
            reg = flows[2] as String?,
            cond = flows[3] as String?,
            minP = flows[4] as Long?,
            maxP = flows[5] as Long?,
            sort = flows[6] as String?
        )
    }.flatMapLatest { filter ->
        repository.searchProducts(
            filter.q, filter.cat, filter.reg, filter.cond, filter.minP, filter.maxP, filter.sort
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Favorites
    @OptIn(ExperimentalCoroutinesApi::class)
    val userFavorites: StateFlow<List<FavoriteEntity>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getFavoritesByUser(user.id)
        else MutableStateFlow(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Seller's own products
    @OptIn(ExperimentalCoroutinesApi::class)
    val sellerProducts: StateFlow<List<ProductEntity>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getProductsBySeller(user.id)
        else MutableStateFlow(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Admin Flow Data
    val pendingSellers: StateFlow<List<UserEntity>> = repository.getPendingSellers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val pendingProducts: StateFlow<List<ProductEntity>> = repository.getPendingProducts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allProductsAdmin: StateFlow<List<ProductEntity>> = repository.getAllProductsAdmin().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allUsersAdmin: StateFlow<List<UserEntity>> = repository.getAllUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allReportsAdmin: StateFlow<List<ReportEntity>> = repository.getAllReports().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // User Conversations & Messages
    @OptIn(ExperimentalCoroutinesApi::class)
    val userConversations: StateFlow<List<ConversationEntity>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getConversationsForUser(user.id)
        else MutableStateFlow(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // User Notifications
    @OptIn(ExperimentalCoroutinesApi::class)
    val userNotifications: StateFlow<List<NotificationEntity>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getNotificationsForUser(user.id)
        else MutableStateFlow(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Active Selected Product Details
    private val _selectedProduct = MutableStateFlow<ProductEntity?>(null)
    val selectedProduct: StateFlow<ProductEntity?> = _selectedProduct.asStateFlow()

    fun selectProduct(product: ProductEntity?) {
        _selectedProduct.value = product
        if (product != null) {
            viewModelScope.launch {
                repository.incrementViews(product.id)
            }
        }
    }

    // Actions
    fun switchUserAccount(user: UserEntity) {
        repository.setCurrentUser(user)
        showToast("Kaonty switched: ${user.fullName} (${user.accountType})")
    }

    fun login(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val success = repository.login(email)
            if (success) {
                onSuccess()
                showToast("Tafiditra soa aman-tsara ianao")
            } else {
                onError("Tsy hita ity adiresy imailaka ity. Mandehana misoratra anarana.")
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        phone: String,
        region: String,
        city: String,
        accountType: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.registerUser(fullName, email, phone, region, city, accountType)
            showToast("Tafiditra soa aman-tsara ny kaontinao")
            onSuccess()
        }
    }

    fun toggleFavorite(productId: String) {
        val user = currentUser.value
        if (user == null) {
            showToast("Mila miditra kaonty ianao mba hampiditra favoris")
            return
        }
        viewModelScope.launch {
            repository.toggleFavorite(user.id, productId)
        }
    }

    fun createListing(
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
    ) {
        val user = currentUser.value
        if (user == null) {
            onError("Mila miditra kaonty ianao")
            return
        }
        if (user.sellerStatus != "APPROVED") {
            onError("Tsy mbola ankatoavina ny kaontinao mpivarotra.")
            return
        }
        val price = priceStr.toLongOrNull()
        if (price == null || price <= 0) {
            onError("Ampidiro vidy marina (Ariary)")
            return
        }
        val quantity = quantityStr.toIntOrNull() ?: 1

        viewModelScope.launch {
            try {
                repository.createProduct(
                    title = title,
                    description = description,
                    price = price,
                    category = category,
                    subcategory = subcategory,
                    condition = condition,
                    quantity = quantity,
                    imageUrls = imageUrls,
                    region = region,
                    city = city,
                    deliveryAvailable = deliveryAvailable,
                    negotiationAvailable = negotiationAvailable
                )
                showToast("Miandry fanamarinana ataon'ny administrateur.")
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Tsy nahomby ny fampidirana entana")
            }
        }
    }

    fun approveSeller(sellerId: String) {
        viewModelScope.launch {
            repository.approveSeller(sellerId)
            showToast("Ankatoavina soa aman-tsara ilay mpivarotra ✅")
        }
    }

    fun rejectSeller(sellerId: String) {
        viewModelScope.launch {
            repository.rejectSeller(sellerId)
            showToast("Gafy / Gafina ilay mpivarotra")
        }
    }

    fun approveProduct(productId: String) {
        viewModelScope.launch {
            repository.updateProductStatus(productId, "APPROVED")
            showToast("Nekena ny entana ary efa hitan'ny rehetra ✅")
        }
    }

    fun rejectProduct(productId: String) {
        viewModelScope.launch {
            repository.updateProductStatus(productId, "REJECTED")
            showToast("Tsy nekena ny entana")
        }
    }

    fun markProductSold(productId: String) {
        viewModelScope.launch {
            repository.markProductAsSold(productId)
            showToast("Voamarka fa efa lafo ny entana ✅")
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            showToast("Voafafa ny entana")
        }
    }

    fun startConversation(
        sellerId: String,
        sellerName: String,
        productId: String,
        productTitle: String,
        productPrice: Long,
        productImage: String,
        onSuccess: (String) -> Unit
    ) {
        val user = currentUser.value
        if (user == null) {
            showToast("Mila miditra kaonty ianao hifandray amin'ny mpivarotra")
            return
        }
        viewModelScope.launch {
            val convId = repository.startOrGetConversation(
                buyerId = user.id,
                buyerName = user.fullName,
                sellerId = sellerId,
                sellerName = sellerName,
                productId = productId,
                productTitle = productTitle,
                productPrice = productPrice,
                productImage = productImage
            )
            onSuccess(convId)
        }
    }

    fun sendMessage(conversationId: String, receiverId: String, messageText: String) {
        val user = currentUser.value ?: return
        if (messageText.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(conversationId, user.id, receiverId, messageText)
        }
    }

    fun submitReport(targetType: String, targetId: String, reason: String, description: String) {
        val user = currentUser.value
        viewModelScope.launch {
            repository.submitReport(
                reporterId = user?.id ?: "anonymous",
                targetType = targetType,
                targetId = targetId,
                reason = reason,
                description = description
            )
            showToast("Voaray ny fitarainanao. Misaotra anao nanampy tamin'ny fiarovana ny tsenanay.")
        }
    }

    fun resetFilters() {
        searchQuery.value = ""
        selectedCategory.value = null
        selectedRegion.value = null
        selectedCondition.value = null
        minPrice.value = null
        maxPrice.value = null
        sortBy.value = "newest"
    }

    private data class TupleFilter(
        val q: String?,
        val cat: String?,
        val reg: String?,
        val cond: String?,
        val minP: Long?,
        val maxP: Long?,
        val sort: String?
    )
}
