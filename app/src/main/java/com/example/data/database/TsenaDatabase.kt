package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.FavoriteDao
import com.example.data.dao.MessageDao
import com.example.data.dao.MonetizationDao
import com.example.data.dao.NotificationDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ProductDao
import com.example.data.dao.ReportDao
import com.example.data.dao.ReviewDao
import com.example.data.dao.UserDao
import com.example.data.entity.ConversationEntity
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.MessageEntity
import com.example.data.entity.MonetizationTransactionEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReportEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        FavoriteEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        ReviewEntity::class,
        ReportEntity::class,
        NotificationEntity::class,
        OrderEntity::class,
        MonetizationTransactionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TsenaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun messageDao(): MessageDao
    abstract fun reviewDao(): ReviewDao
    abstract fun reportDao(): ReportDao
    abstract fun notificationDao(): NotificationDao
    abstract fun orderDao(): OrderDao
    abstract fun monetizationDao(): MonetizationDao

    companion object {
        @Volatile
        private var INSTANCE: TsenaDatabase? = null

        fun getInstance(context: Context): TsenaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TsenaDatabase::class.java,
                    "tsena_malagasy_database.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        private suspend fun populateDatabase(db: TsenaDatabase) {
            // Seed Demo Users
            DatabaseSeedData.INITIAL_USERS.forEach { db.userDao().insertUser(it) }

            // Seed Demo Products
            DatabaseSeedData.INITIAL_PRODUCTS.forEach { db.productDao().insertProduct(it) }

            // Seed Demo Conversations & Messages
            DatabaseSeedData.INITIAL_CONVERSATIONS.forEach { db.messageDao().insertConversation(it) }
            DatabaseSeedData.INITIAL_MESSAGES.forEach { db.messageDao().insertMessage(it) }

            // Seed Demo Notifications
            DatabaseSeedData.INITIAL_NOTIFICATIONS.forEach { db.notificationDao().insertNotification(it) }

            // Seed Demo Reviews
            DatabaseSeedData.INITIAL_REVIEWS.forEach { db.reviewDao().insertReview(it) }
        }
    }
}
