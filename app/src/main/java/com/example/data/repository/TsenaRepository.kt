package com.example.data.repository

import com.example.data.database.DatabaseSeedData
import com.example.data.database.TsenaDatabase
import com.example.data.entity.ConversationEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.MessageEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReportEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TsenaRepository(private val database: TsenaDatabase) {

    // Current Authenticated User Session (Defaults to Buyer_001 or Admin if switched)
    private val _currentUser = MutableStateFlow<UserEntity?>(DatabaseSeedData.DEFAULT_BUYER)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: UserEntity?) {
        _currentUser.value = user
    }

    suspend fun login(email: String): Boolean {
        val user = database.userDao().getUserByEmail(email)
        return if (user != null) {
            _currentUser.value = user
            true
        } else {
            false
        }
    }

    suspend fun registerUser(
        fullName: String,
        email: String,
        phone: String,
        region: String,
        city: String,
        accountType: String
    ): UserEntity {
        val newUserId = "user_${System.currentTimeMillis()}"
        val sellerStatus = if (accountType == "SELLER") "PENDING_APPROVAL" else "APPROVED"
        val newUser = UserEntity(
            id = newUserId,
            fullName = fullName,
            email = email,
            phone = phone,
            region = region,
            city = city,
            accountType = accountType,
            sellerStatus = sellerStatus,
            isVerifiedSeller = false
        )
        database.userDao().insertUser(newUser)
        _currentUser.value = newUser

        if (accountType == "SELLER") {
            // Notify user about pending seller approval
            database.notificationDao().insertNotification(
                NotificationEntity(
                    userId = newUserId,
                    title = "Fankatoavana mpivarotra ilaina",
                    message = "Mba hahafahanao mivarotra ao amin'ny TSENA MALAGASY dia mila ankatoavin'ny administrateur aloha ianao. Mifandraisa aminay amin'ny WhatsApp (+261 38 56 513 78).",
                    type = "SELLER_APPROVAL"
                )
            )
        }
        return newUser
    }

    // Products
    fun getApprovedProducts(): Flow<List<ProductEntity>> = database.productDao().getApprovedProducts()

    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>> = database.productDao().getProductsBySeller(sellerId)

    fun getPendingProducts(): Flow<List<ProductEntity>> = database.productDao().getPendingProducts()

    fun getAllProductsAdmin(): Flow<List<ProductEntity>> = database.productDao().getAllProductsAdmin()

    fun getProductById(productId: String): Flow<ProductEntity?> = database.productDao().getProductById(productId)

    fun searchProducts(
        query: String?,
        category: String?,
        region: String?,
        condition: String?,
        minPrice: Long?,
        maxPrice: Long?,
        sortBy: String?
    ): Flow<List<ProductEntity>> {
        val cleanQuery = if (query.isNull_or_blank()) null else query?.trim()
        val cleanCat = if (category.isNull_or_blank() || category == "Tsy misy" || category == "Rehetra") null else category
        val cleanReg = if (region.isNull_or_blank() || region == "Rehetra") null else region
        val cleanCond = if (condition.isNull_or_blank() || condition == "Rehetra") null else condition
        return database.productDao().searchProducts(
            cleanQuery, cleanCat, cleanReg, cleanCond, minPrice, maxPrice, sortBy
        )
    }

    private fun String?.isNull_or_blank(): Boolean = this == null || this.isBlank()

    suspend fun createProduct(
        title: String,
        description: String,
        price: Long,
        category: String,
        subcategory: String,
        condition: String,
        quantity: Int,
        imageUrls: String,
        region: String,
        city: String,
        deliveryAvailable: Boolean,
        negotiationAvailable: Boolean
    ): String {
        val user = _currentUser.value ?: throw IllegalStateException("Tsy tafiditra ianao")
        if (user.sellerStatus != "APPROVED") {
            throw IllegalStateException("Tsy mbola ankatoavina ny kaontinao mpivarotra.")
        }

        val productId = "prod_${System.currentTimeMillis()}"
        val product = ProductEntity(
            id = productId,
            sellerId = user.id,
            sellerName = user.fullName,
            sellerPhone = user.phone,
            sellerRegion = user.region,
            sellerCity = user.city,
            sellerVerified = user.isVerifiedSeller,
            title = title,
            description = description,
            price = price,
            category = category,
            subcategory = subcategory,
            condition = condition,
            quantity = quantity,
            imageUrls = if (imageUrls.isBlank()) "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600" else imageUrls,
            region = region,
            city = city,
            deliveryAvailable = deliveryAvailable,
            negotiationAvailable = negotiationAvailable,
            status = "PENDING_REVIEW" // Pending Admin approval per rule 8 & 43!
        )
        database.productDao().insertProduct(product)

        // Add notification to seller
        database.notificationDao().insertNotification(
            NotificationEntity(
                userId = user.id,
                title = "Miarandry fanamarinana ny entanao",
                message = "Ny entana '$title' dia miandry fankatoavana avy amin'ny administrateur.",
                type = "LISTING_PENDING"
            )
        )

        return productId
    }

    suspend fun updateProductStatus(productId: String, status: String) {
        database.productDao().updateProductStatus(productId, status)
        val product = database.productDao().getProductByIdSync(productId)
        if (product != null) {
            val notifyMsg = if (status == "APPROVED") {
                "Arahabaina! Nekena sy nivoaka ho hitan'ny rehetra ny entanao '${product.title}'."
            } else if (status == "REJECTED") {
                "Mampahafantatra fa tsy nekena ny entanao '${product.title}'."
            } else {
                "Niova ny satan'ny entana '${product.title}'."
            }
            database.notificationDao().insertNotification(
                NotificationEntity(
                    userId = product.sellerId,
                    title = "Satan'ny entana",
                    message = notifyMsg,
                    type = "LISTING_STATUS"
                )
            )
        }
    }

    suspend fun markProductAsSold(productId: String) {
        database.productDao().updateProductStatus(productId, "SOLD")
    }

    suspend fun deleteProduct(productId: String) {
        database.productDao().deleteProduct(productId)
    }

    suspend fun incrementViews(productId: String) {
        database.productDao().incrementViews(productId)
    }

    // Favorites
    fun getFavoritesByUser(userId: String): Flow<List<FavoriteEntity>> = database.favoriteDao().getFavoritesByUser(userId)

    fun isFavorite(userId: String, productId: String): Flow<Boolean> = database.favoriteDao().isFavorite(userId, productId)

    suspend fun toggleFavorite(userId: String, productId: String) {
        val exists = database.favoriteDao().isFavoriteSync(userId, productId)
        if (exists) {
            database.favoriteDao().removeFavorite(userId, productId)
            database.productDao().updateFavoritesCount(productId, -1)
        } else {
            database.favoriteDao().addFavorite(FavoriteEntity(userId = userId, productId = productId))
            database.productDao().updateFavoritesCount(productId, 1)
        }
    }

    // Admin Operations
    fun getPendingSellers(): Flow<List<UserEntity>> = database.userDao().getPendingSellers()

    fun getAllUsers(): Flow<List<UserEntity>> = database.userDao().getAllUsers()

    fun getAllSellers(): Flow<List<UserEntity>> = database.userDao().getAllSellers()

    suspend fun approveSeller(sellerId: String) {
        database.userDao().updateSellerStatus(sellerId, "APPROVED", isVerified = true)
        database.notificationDao().insertNotification(
            NotificationEntity(
                userId = sellerId,
                title = "Arahabaina! Nekena ny kaontinao mpivarotra ✅",
                message = "Nekena ho mpivarotra ao amin'ny TSENA MALAGASY ianao. Afaka mamoaka entana varotana ianao izao.",
                type = "SELLER_APPROVAL"
            )
        )
    }

    suspend fun rejectSeller(sellerId: String) {
        database.userDao().updateSellerStatus(sellerId, "REJECTED", isVerified = false)
        database.notificationDao().insertNotification(
            NotificationEntity(
                userId = sellerId,
                title = "Fangatahana mpivarotra",
                message = "Tsy nekena ny fangatahanao ho mpivarotra. Mifandraisa amin'ny administrateur amin'ny WhatsApp (+261 38 56 513 78).",
                type = "SELLER_APPROVAL"
            )
        )
    }

    // Messaging & Conversations
    fun getConversationsForUser(userId: String): Flow<List<ConversationEntity>> = database.messageDao().getConversationsForUser(userId)

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> = database.messageDao().getMessages(conversationId)

    suspend fun startOrGetConversation(
        buyerId: String,
        buyerName: String,
        sellerId: String,
        sellerName: String,
        productId: String,
        productTitle: String,
        productPrice: Long,
        productImage: String
    ): String {
        val existing = database.messageDao().findConversation(buyerId, sellerId, productId)
        if (existing != null) {
            return existing.id
        }

        val conversationId = "conv_${System.currentTimeMillis()}"
        val newConv = ConversationEntity(
            id = conversationId,
            buyerId = buyerId,
            buyerName = buyerName,
            sellerId = sellerId,
            sellerName = sellerName,
            productId = productId,
            productTitle = productTitle,
            productPrice = productPrice,
            productImage = productImage,
            lastMessage = "Resaka vao natomboka..."
        )
        database.messageDao().insertConversation(newConv)
        return conversationId
    }

    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        receiverId: String,
        messageText: String
    ) {
        val msg = MessageEntity(
            conversationId = conversationId,
            senderId = senderId,
            receiverId = receiverId,
            message = messageText
        )
        database.messageDao().insertMessage(msg)
        database.messageDao().updateConversationLastMessage(conversationId, messageText, System.currentTimeMillis())

        // Notify receiver
        database.notificationDao().insertNotification(
            NotificationEntity(
                userId = receiverId,
                title = "Hafatra vaovao 💬",
                message = messageText.take(50),
                type = "NEW_MESSAGE"
            )
        )
    }

    // Notifications
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>> = database.notificationDao().getNotificationsForUser(userId)

    suspend fun markNotificationsAsRead(userId: String) {
        database.notificationDao().markAllAsRead(userId)
    }

    // Reviews & Reports
    fun getReviewsForSeller(sellerId: String): Flow<List<ReviewEntity>> = database.reviewDao().getReviewsForSeller(sellerId)

    suspend fun addReview(sellerId: String, buyerId: String, buyerName: String, rating: Int, comment: String) {
        database.reviewDao().insertReview(
            ReviewEntity(
                sellerId = sellerId,
                buyerId = buyerId,
                buyerName = buyerName,
                rating = rating,
                comment = comment
            )
        )
    }

    fun getAllReports(): Flow<List<ReportEntity>> = database.reportDao().getAllReports()

    suspend fun submitReport(reporterId: String, targetType: String, targetId: String, reason: String, description: String) {
        database.reportDao().insertReport(
            ReportEntity(
                reporterId = reporterId,
                targetType = targetType,
                targetId = targetId,
                reason = reason,
                description = description
            )
        )
    }

    // Orders Architecture
    fun getOrdersForUser(userId: String): Flow<List<OrderEntity>> = database.orderDao().getOrdersForUser(userId)

    suspend fun createOrder(
        buyerId: String,
        sellerId: String,
        productId: String,
        productTitle: String,
        quantity: Int,
        totalAmount: Long,
        paymentMethod: String,
        shippingAddress: String
    ): String {
        val orderId = "ORD_${System.currentTimeMillis()}"
        val order = OrderEntity(
            orderId = orderId,
            buyerId = buyerId,
            sellerId = sellerId,
            productId = productId,
            productTitle = productTitle,
            quantity = quantity,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            paymentStatus = "PENDING",
            deliveryStatus = "PENDING",
            shippingAddress = shippingAddress
        )
        database.orderDao().insertOrder(order)
        return orderId
    }
}
