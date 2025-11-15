package com.code.data.models.response.product

import com.google.gson.annotations.SerializedName

/**
 * Data class that holds product details in the Description screen
 */
data class ProductDetails(
    @SerializedName("additionalMedia")
    val productImages: List<AdditionalMedia> = listOf(),
    val altLangSeoText: String = "",
    val availability: Availability = Availability(),
    val brandName: String = "",
    val brandThumbnailImage: String = "",
    val categoryName: String = "",
    val currentRegion: String = "",
    val hasFreeShipping: Boolean = false,
    val hasFrenchContent: Boolean = false,
    val hasHomeDeliveryService: Boolean = false,
    val hasInStorePickup: Boolean = false,
    val hasPromotion: Boolean = false,
    val hasRebate: Boolean = false,
    val hasWarranty: Boolean = false,
    val hideSaleEndDate: Boolean = false,
    val hideSavings: Boolean = false,
    val highResImage: String = "",
    val isAdvertised: Boolean = false,
    val isAvailableForOrder: Boolean = false,
    val isAvailableForPickup: Boolean = false,
    val isBackorderable: Boolean = false,
    val isClearance: Boolean = false,
    val isInStoreOnly: Boolean = false,
    val isMachineTranslated: Boolean = false,
    val isMarketplace: Boolean = false,
    val isOnlineOnly: Boolean = false,
    val isPreorderable: Boolean = false,
    val isPriceEndsLabel: Boolean = false,
    val isProductOnSale: Boolean = false,
    val isPurchasable: Boolean = false,
    val isShippable: Boolean = false,
    val isSpecialDelivery: Boolean = false,
    val isVisible: Boolean = false,
    val longDescription: String = "",
    val make: String = "",
    val manufacturer: String = "",
    val modelNumber: String = "",
    val name: String = "",
    val offerId: String = "",
    val preorderOrderDate: String = "",
    val primaryParentCategoryId: String = "",
    val productType: String = "",
    val productUrl: String = "",
    val regularPrice: Double = 0.0,
    val requiresAgeVerification: Boolean = false,
    val saleEndDate: String = "",
    val salePrice: Double = 0.0,
    val saleStartDate: String = "",
    val sellerId: String = "",
    val seoText: String = "",
    val shortDescription: String = "",
    val sku: String = "",
    val specs: List<Spec> = listOf(),
    val thumbnailImage: String = "",
    val upcNumber: String = "",
    val warranties: List<Warranty> = listOf(),
    val warrantyAndRepairDisclosureUrl: String = "",
    val whatsInTheBox: List<String> = listOf()
)

data class Availability(
    val buttonState: String = "",
    val inStoreAvailability: String = "",
    val inStoreAvailabilityText: String = "",
    val inStoreAvailabilityUpdateDate: String = "",
    val isAvailableOnline: Boolean = false,
    val onlineAvailability: String = "",
    val onlineAvailabilityCount: Int = 0,
    val onlineAvailabilityText: String = "",
    val onlineAvailabilityUpdateDate: String = "",
    val sku: String = ""
)

data class AdditionalMedia(
    val mimeType: String = "",
    val thumbnailUrl: String = "",
    val url: String = ""
)

data class Spec(
    val group: String = "",
    val name: String = "",
    val value: String = ""
)

data class Warranty(
    val parentSku: String = "",
    val regularPrice: Double = 0.0,
    val sku: String = "",
    val subType: String = "",
    val termMonths: Int = 0,
    val title: String = "",
    val type: String = ""
)