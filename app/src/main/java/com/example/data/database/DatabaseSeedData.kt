package com.example.data.database

import com.example.data.entity.ConversationEntity
import com.example.data.entity.MessageEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.ProductEntity
import com.example.data.entity.ReviewEntity
import com.example.data.entity.UserEntity

object DatabaseSeedData {

    val ADMIN_USER = UserEntity(
        id = "admin_001",
        fullName = "Administrateur TSENA",
        email = "admin@tsenamalagasy.mg",
        phone = "+261 38 56 513 78",
        region = "Analamanga (Antananarivo)",
        city = "Antananarivo Renivohitra",
        accountType = "ADMIN",
        sellerStatus = "APPROVED",
        isVerifiedSeller = true,
        rating = 5.0f,
        reviewCount = 10
    )

    val APPROVED_SELLER_1 = UserEntity(
        id = "seller_001",
        fullName = "Rakoto Jean - Tsaralalana Tech",
        email = "rakoto@tsenaralalana.mg",
        phone = "+261 34 12 345 67",
        region = "Analamanga (Antananarivo)",
        city = "Antananarivo Renivohitra",
        accountType = "SELLER",
        sellerStatus = "APPROVED",
        isVerifiedSeller = true,
        rating = 4.8f,
        reviewCount = 15
    )

    val APPROVED_SELLER_2 = UserEntity(
        id = "seller_002",
        fullName = "Rasoa Marie - Toliara Craft",
        email = "rasoa@toliara.mg",
        phone = "+261 32 87 654 32",
        region = "Atsimo-Andrefana (Toliara)",
        city = "Toliara I",
        accountType = "SELLER",
        sellerStatus = "APPROVED",
        isVerifiedSeller = true,
        rating = 4.9f,
        reviewCount = 8
    )

    val PENDING_SELLER = UserEntity(
        id = "seller_003",
        fullName = "Andry Solo - Tamatave Agri",
        email = "andry@tamatave.mg",
        phone = "+261 33 55 123 99",
        region = "Atsinanana (Toamasina)",
        city = "Toamasina I",
        accountType = "SELLER",
        sellerStatus = "PENDING_APPROVAL",
        isVerifiedSeller = false,
        rating = 5.0f,
        reviewCount = 0
    )

    val DEFAULT_BUYER = UserEntity(
        id = "buyer_001",
        fullName = "Mialy Randria",
        email = "mialy@gmail.com",
        phone = "+261 34 88 990 11",
        region = "Vakinankaratra (Antsirabe)",
        city = "Antsirabe I",
        accountType = "BUYER",
        sellerStatus = "PENDING_APPROVAL",
        isVerifiedSeller = false
    )

    val INITIAL_USERS = listOf(
        ADMIN_USER,
        APPROVED_SELLER_1,
        APPROVED_SELLER_2,
        PENDING_SELLER,
        DEFAULT_BUYER
    )

    val INITIAL_PRODUCTS = listOf(
        ProductEntity(
            id = "prod_101",
            sellerId = "seller_001",
            sellerName = "Rakoto Jean - Tsaralalana Tech",
            sellerPhone = "+261 34 12 345 67",
            sellerRegion = "Analamanga (Antananarivo)",
            sellerCity = "Antananarivo Renivohitra",
            sellerVerified = true,
            title = "iPhone 13 Pro 256GB Gold (Etat Impeccable)",
            description = "Finday iPhone 13 Pro 256GB vao avy any ivelany, bateria 92%, misy boîte sy chargeur d'origine. Mbola tsara be tsy misy rax.",
            price = 2800000,
            category = "Téléphones & Électronique",
            subcategory = "Smartphone",
            condition = "Neuf",
            quantity = 2,
            imageUrls = "https://images.unsplash.com/photo-1632661674596-df8be070a5c5?w=600",
            region = "Analamanga (Antananarivo)",
            city = "Antananarivo Renivohitra",
            deliveryAvailable = true,
            negotiationAvailable = true,
            status = "APPROVED",
            views = 142,
            favoritesCount = 18
        ),
        ProductEntity(
            id = "prod_102",
            sellerId = "seller_001",
            sellerName = "Rakoto Jean - Tsaralalana Tech",
            sellerPhone = "+261 34 12 345 67",
            sellerRegion = "Analamanga (Antananarivo)",
            sellerCity = "Antananarivo Renivohitra",
            sellerVerified = true,
            title = "Dell XPS 13 i7 16GB RAM 512GB SSD",
            description = "Kajimirindra Dell XPS 13 matanjaka be amin'ny asa birao, programmation, sy design. Ecran Full HD IPS, clavier retroeclaire.",
            price = 3500000,
            category = "Ordinateurs",
            subcategory = "PC Portable",
            condition = "Occasion",
            quantity = 1,
            imageUrls = "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=600",
            region = "Analamanga (Antananarivo)",
            city = "Antananarivo Renivohitra",
            deliveryAvailable = true,
            negotiationAvailable = true,
            status = "APPROVED",
            views = 98,
            favoritesCount = 12
        ),
        ProductEntity(
            id = "prod_103",
            sellerId = "seller_002",
            sellerName = "Rasoa Marie - Toliara Craft",
            sellerPhone = "+261 32 87 654 32",
            sellerRegion = "Atsimo-Andrefana (Toliara)",
            sellerCity = "Toliara I",
            sellerVerified = true,
            title = "Lamba Landy Malagasy Nofatenana Amin'ny Tanana",
            description = "Akanjo lamba landy Malagasy tena izy avy amin'ny mpanao asa tanana. Tsara ho an'ny lanonana sy fety Malagasy.",
            price = 180000,
            category = "Vêtements & Chaussures",
            subcategory = "Lamba traditionnel",
            condition = "Neuf",
            quantity = 5,
            imageUrls = "https://images.unsplash.com/photo-1609357605129-26f69add5d6e?w=600",
            region = "Atsimo-Andrefana (Toliara)",
            city = "Toliara I",
            deliveryAvailable = true,
            negotiationAvailable = false,
            status = "APPROVED",
            views = 210,
            favoritesCount = 34
        ),
        ProductEntity(
            id = "prod_104",
            sellerId = "seller_002",
            sellerName = "Rasoa Marie - Toliara Craft",
            sellerPhone = "+261 32 87 654 32",
            sellerRegion = "Atsimo-Andrefana (Toliara)",
            sellerCity = "Toliara I",
            sellerVerified = true,
            title = "Vanille Naturelle Gousse Bourbon Sambava (1kg)",
            description = "Vanille gousse pure d'origine Sambava Madagascar. Kalitao voalohany (Grade A), fofona mahery sy mampateza.",
            price = 450000,
            category = "Agriculture & Élevage",
            subcategory = "Epices",
            condition = "Neuf",
            quantity = 10,
            imageUrls = "https://images.unsplash.com/photo-1509358211563-80f4ceb548ad?w=600",
            region = "SAVA (Sambava)",
            city = "Sambava",
            deliveryAvailable = true,
            negotiationAvailable = true,
            status = "APPROVED",
            views = 315,
            favoritesCount = 45
        ),
        ProductEntity(
            id = "prod_105",
            sellerId = "seller_001",
            sellerName = "Rakoto Jean - Tsaralalana Tech",
            sellerPhone = "+261 34 12 345 67",
            sellerRegion = "Analamanga (Antananarivo)",
            sellerCity = "Antananarivo Renivohitra",
            sellerVerified = true,
            title = "Scooter Honda DIO 110cc Mbola Tsy Niasa Eto",
            description = "Moto scooter Honda DIO 110cc vao avy nodimandry avy any Japana, moteur d'origine 100%, consommation kely be.",
            price = 4200000,
            category = "Véhicules & Motos",
            subcategory = "Scooter",
            condition = "Occasion",
            quantity = 1,
            imageUrls = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=600",
            region = "Analamanga (Antananarivo)",
            city = "Antananarivo Renivohitra",
            deliveryAvailable = false,
            negotiationAvailable = true,
            status = "APPROVED",
            views = 520,
            favoritesCount = 68
        ),
        ProductEntity(
            id = "prod_106",
            sellerId = "seller_003",
            sellerName = "Andry Solo - Tamatave Agri",
            sellerPhone = "+261 33 55 123 99",
            sellerRegion = "Atsinanana (Toamasina)",
            sellerCity = "Toamasina I",
            sellerVerified = false,
            title = "Vary Gasy Makalioka Gony 50kg Alaotra",
            description = "Vary gasy makalioka madio tsara avy any Alaotra Mangoro. Tsy misy vato na loto. Gony 50kg.",
            price = 135000,
            category = "Agriculture & Élevage",
            subcategory = "Céréales",
            condition = "Neuf",
            quantity = 20,
            imageUrls = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=600",
            region = "Alaotra-Mangoro (Ambatondrazaka)",
            city = "Ambatondrazaka",
            deliveryAvailable = true,
            negotiationAvailable = true,
            status = "PENDING_REVIEW", // Visible in Admin product moderation
            views = 12,
            favoritesCount = 1
        ),
        ProductEntity(
            id = "prod_107",
            sellerId = "seller_001",
            sellerName = "Rakoto Jean - Tsaralalana Tech",
            sellerPhone = "+261 34 12 345 67",
            sellerRegion = "Analamanga (Antananarivo)",
            sellerCity = "Antananarivo Renivohitra",
            sellerVerified = true,
            title = "Menaka Ravintsara Bio Pure (100ml)",
            description = "Menaka ravintsara vita eto Madagasikara, 100% bio ary fanafody sy kajy fahasalamana tsara be.",
            price = 25000,
            category = "Beauté & Cosmétique",
            subcategory = "Huiles essentielles",
            condition = "Neuf",
            quantity = 15,
            imageUrls = "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=600",
            region = "Analamanga (Antananarivo)",
            city = "Antananarivo Renivohitra",
            deliveryAvailable = true,
            negotiationAvailable = false,
            status = "APPROVED",
            views = 175,
            favoritesCount = 29
        )
    )

    val INITIAL_CONVERSATIONS = listOf(
        ConversationEntity(
            id = "conv_1",
            buyerId = "buyer_001",
            buyerName = "Mialy Randria",
            sellerId = "seller_001",
            sellerName = "Rakoto Jean - Tsaralalana Tech",
            productId = "prod_101",
            productTitle = "iPhone 13 Pro 256GB Gold",
            productPrice = 2800000,
            productImage = "https://images.unsplash.com/photo-1632661674596-df8be070a5c5?w=600",
            lastMessage = "Manahoana, mbola misy ve ity iPhone 13 Pro ity?",
            updatedAt = System.currentTimeMillis() - 3600000,
            unreadCount = 1
        )
    )

    val INITIAL_MESSAGES = listOf(
        MessageEntity(
            conversationId = "conv_1",
            senderId = "buyer_001",
            receiverId = "seller_001",
            message = "Salama Jean, mbola afaka miady varotra kely ve ny vidin'ny iPhone 13 Pro?",
            timestamp = System.currentTimeMillis() - 7200000,
            isRead = true
        ),
        MessageEntity(
            conversationId = "conv_1",
            senderId = "seller_001",
            receiverId = "buyer_001",
            message = "Salama Mialy! Eny afaka miady varotra hatramin'ny 2 700 000 Ar raha alainao anio.",
            timestamp = System.currentTimeMillis() - 3600000,
            isRead = false
        )
    )

    val INITIAL_NOTIFICATIONS = listOf(
        NotificationEntity(
            userId = "buyer_001",
            title = "Tongasoa eto amin'ny TSENA MALAGASY!",
            message = "Misaotra anao nampiasa ny tsenanay. Afaka mikaroka, mividy na mivarotra entana mora eto Madagasikara ianao.",
            timestamp = System.currentTimeMillis() - 86400000,
            isRead = false,
            type = "SYSTEM"
        ),
        NotificationEntity(
            userId = "seller_003",
            title = "Fankatoavana mpivarotra ilaina",
            message = "Mba hahafahanao mivarotra ao amin'ny TSENA MALAGASY dia mila ankatoavin'ny administrateur aloha ianao. Mifandraisa aminay amin'ny WhatsApp (+261 38 56 513 78).",
            timestamp = System.currentTimeMillis() - 43200000,
            isRead = false,
            type = "SELLER_APPROVAL"
        )
    )

    val INITIAL_REVIEWS = listOf(
        ReviewEntity(
            sellerId = "seller_001",
            buyerId = "buyer_001",
            buyerName = "Mialy Randria",
            rating = 5,
            comment = "Mpivarotra tena azo antoka! Nahazo ilay entana ara-potoana sy mendrika tsara.",
            timestamp = System.currentTimeMillis() - 172800000
        )
    )
}
