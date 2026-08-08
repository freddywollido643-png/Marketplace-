package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val region: String,
    val city: String,
    val accountType: String, // BUYER, SELLER, ADMIN
    val sellerStatus: String, // PENDING_APPROVAL, APPROVED, REJECTED
    val isVerifiedSeller: Boolean = false,
    val rating: Float = 5.0f,
    val reviewCount: Int = 0,
    val profilePic: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val sellerId: String,
    val sellerName: String,
    val sellerPhone: String,
    val sellerRegion: String,
    val sellerCity: String,
    val sellerVerified: Boolean,
    val title: String,
    val description: String,
    val price: Long, // Ariary (Ar)
    val category: String,
    val subcategory: String,
    val condition: String, // Neuf, Occasion
    val quantity: Int = 1,
    val imageUrls: String, // Separated by comma or JSON
    val region: String,
    val city: String,
    val deliveryAvailable: Boolean = true,
    val negotiationAvailable: Boolean = true,
    val status: String, // DRAFT, PENDING_REVIEW, APPROVED, REJECTED, SOLD, ARCHIVED
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val views: Int = 0,
    val favoritesCount: Int = 0
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val productId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val buyerId: String,
    val buyerName: String,
    val sellerId: String,
    val sellerName: String,
    val productId: String,
    val productTitle: String,
    val productPrice: Long,
    val productImage: String,
    val lastMessage: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sellerId: String,
    val buyerId: String,
    val buyerName: String,
    val rating: Int,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reporterId: String,
    val targetType: String, // PRODUCT, SELLER, USER
    val targetId: String,
    val reason: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING" // PENDING, RESOLVED
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "INFO" // SELLER_APPROVAL, LISTING_APPROVED, NEW_MESSAGE, SYSTEM
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val buyerId: String,
    val sellerId: String,
    val productId: String,
    val productTitle: String,
    val quantity: Int,
    val totalAmount: Long, // Ariary
    val paymentMethod: String, // MVOLA, ORANGE_MONEY, AIRTEL_MONEY, CASH
    val paymentStatus: String, // PENDING, SUCCESS, FAILED
    val deliveryStatus: String, // PENDING, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED
    val shippingAddress: String,
    val createdAt: Long = System.currentTimeMillis()
)
