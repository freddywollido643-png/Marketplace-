package com.example.data.model

enum class AccountType {
    BUYER,
    SELLER,
    ADMIN
}

enum class SellerStatus {
    PENDING_APPROVAL,
    APPROVED,
    REJECTED
}

enum class ProductStatus {
    DRAFT,
    PENDING_REVIEW,
    APPROVED,
    REJECTED,
    SOLD,
    ARCHIVED
}

enum class ProductCondition {
    NEUF,
    OCCASION
}

val MADAGASCAR_REGIONS = listOf(
    "Analamanga (Antananarivo)",
    "Vakinankaratra (Antsirabe)",
    "Itasy (Miarinarivo)",
    "Bongolava (Tsiroanomandidy)",
    "Matsiatra Ambony (Fianarantsoa)",
    "Amoron'i Mania (Ambositra)",
    "Vatovavy (Mananjary)",
    "Fitovinany (Manakara)",
    "Atsimo-Atsinanana (Farafangana)",
    "Ihorombe (Ihosy)",
    "Atsinanana (Toamasina)",
    "Analanjirofo (Fenerive Est)",
    "Alaotra-Mangoro (Ambatondrazaka)",
    "Boeny (Mahajanga)",
    "Sofia (Antsohihy)",
    "Betsiboka (Maevatanana)",
    "DIANA (Antsiranana)",
    "SAVA (Sambava)",
    "Atsimo-Andrefana (Toliara)",
    "Androy (Ambovombe)",
    "Anosy (Tôlanaro)",
    "Menabe (Morondava)",
    "Melaky (Maintirano)"
)

data class CategoryInfo(
    val id: String,
    val nameMg: String,
    val nameFr: String,
    val iconName: String,
    val description: String
)

val DEFAULT_CATEGORIES = listOf(
    CategoryInfo("cat_1", "Téléphones & Électronique", "Finday sy Elektronika", "phone_android", "Finday, Tablette, Phare, Accessoires"),
    CategoryInfo("cat_2", "Ordinateurs", "Kajimirindra", "computer", "PC Portable, Ordinateur de bureau, Imprimante"),
    CategoryInfo("cat_3", "Vêtements & Chaussures", "Akanjo sy Kiraro", "checkroom", "Akanjo lehilahy, vavy, ankizy, kiraro"),
    CategoryInfo("cat_4", "Maison & Mobilier", "Trano sy Fanaka", "home", "Kabinetra, fandriana, seza, lafaoro"),
    CategoryInfo("cat_5", "Véhicules & Motos", "Fiara sy Moto", "directions_car", "Fiara, Scooter, Moto, Piese rehetra"),
    CategoryInfo("cat_6", "Agriculture & Élevage", "Fambolena sy Fiompiana", "agriculture", "Vary, katsaka, vanille, biby fiompy"),
    CategoryInfo("cat_7", "Alimentation", "Sakafo sy Zava-pisotro", "restaurant", "Sakafo masaka, vokatra avy amin'ny tantsaha"),
    CategoryInfo("cat_8", "Beauté & Cosmétique", "Kajy tarehy sy Siansa", "spa", "Savony, menaka madagasikara, makiazy"),
    CategoryInfo("cat_9", "Matériaux de construction", "Akora fanorenana", "construction", "Sima, vy, brikina, hazo"),
    CategoryInfo("cat_10", "Immobilier", "Trano sy Tany", "domain", "Tany ho varotana, trano hofana"),
    CategoryInfo("cat_11", "Services", "Tolotra sy Asa", "build", "Kajy trano, reparation, hair craft"),
    CategoryInfo("cat_12", "Emploi", "Asa sy Fandraisana", "work", "Tolotra asa, fikarohana asa"),
    CategoryInfo("cat_13", "Livres & Formation", "Boky sy Fampiofanana", "menu_book", "Boky, fampianarana teny, computer training"),
    CategoryInfo("cat_14", "Autres", "Entana hafa", "more_horiz", "Entana samihafa")
)
