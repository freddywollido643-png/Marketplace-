package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserByIdSync(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE accountType = 'SELLER' AND sellerStatus = 'PENDING_APPROVAL'")
    fun getPendingSellers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE accountType = 'SELLER'")
    fun getAllSellers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET sellerStatus = :status, isVerifiedSeller = :isVerified WHERE id = :userId")
    suspend fun updateSellerStatus(userId: String, status: String, isVerified: Boolean)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE status = 'APPROVED' ORDER BY createdAt DESC")
    fun getApprovedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductByIdSync(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE status = 'PENDING_REVIEW' ORDER BY createdAt DESC")
    fun getPendingProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProductsAdmin(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category AND status = 'APPROVED' ORDER BY createdAt DESC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("""
        SELECT * FROM products 
        WHERE status = 'APPROVED' 
        AND (:query IS NULL OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        AND (:category IS NULL OR category = :category)
        AND (:region IS NULL OR region = :region)
        AND (:condition IS NULL OR condition = :condition)
        AND (:minPrice IS NULL OR price >= :minPrice)
        AND (:maxPrice IS NULL OR price <= :maxPrice)
        ORDER BY 
        CASE WHEN :sortBy = 'price_low' THEN price END ASC,
        CASE WHEN :sortBy = 'price_high' THEN price END DESC,
        createdAt DESC
    """)
    fun searchProducts(
        query: String?,
        category: String?,
        region: String?,
        condition: String?,
        minPrice: Long?,
        maxPrice: Long?,
        sortBy: String?
    ): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET status = :status WHERE id = :productId")
    suspend fun updateProductStatus(productId: String, status: String)

    @Query("UPDATE products SET views = views + 1 WHERE id = :productId")
    suspend fun incrementViews(productId: String)

    @Query("UPDATE products SET favoritesCount = favoritesCount + :delta WHERE id = :productId")
    suspend fun updateFavoritesCount(productId: String, delta: Int)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: String)
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY createdAt DESC")
    fun getFavoritesByUser(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND productId = :productId)")
    fun isFavorite(userId: String, productId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND productId = :productId)")
    suspend fun isFavoriteSync(userId: String, productId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND productId = :productId")
    suspend fun removeFavorite(userId: String, productId: String)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM conversations WHERE buyerId = :userId OR sellerId = :userId ORDER BY updatedAt DESC")
    fun getConversationsForUser(userId: String): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    fun getConversationById(conversationId: String): Flow<ConversationEntity?>

    @Query("SELECT * FROM conversations WHERE (buyerId = :user1 AND sellerId = :user2 AND productId = :productId) OR (buyerId = :user2 AND sellerId = :user1 AND productId = :productId) LIMIT 1")
    suspend fun findConversation(user1: String, user2: String, productId: String): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("UPDATE conversations SET lastMessage = :lastMessage, updatedAt = :timestamp WHERE id = :conversationId")
    suspend fun updateConversationLastMessage(conversationId: String, lastMessage: String, timestamp: Long)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE sellerId = :sellerId ORDER BY timestamp DESC")
    fun getReviewsForSeller(sellerId: String): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("UPDATE reports SET status = :status WHERE id = :reportId")
    suspend fun updateReportStatus(reportId: Long, status: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE buyerId = :userId OR sellerId = :userId ORDER BY createdAt DESC")
    fun getOrdersForUser(userId: String): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET deliveryStatus = :status WHERE orderId = :orderId")
    suspend fun updateDeliveryStatus(orderId: String, status: String)
}
