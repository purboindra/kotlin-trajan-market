package com.example.trajanmarket.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("limit")
    val limit: Int,
    @SerialName("products")
    val products: List<Product>,
    @SerialName("skip")
    val skip: Int,
    @SerialName("total")
    val total: Int
) {
    @Serializable
    data class Product(
        @SerialName("appwrite_id")
        val appwriteId: String = "",
        @SerialName("availabilityStatus")
        val availabilityStatus: String="",
        @SerialName("brand")
        val brand: String = "",
        @SerialName("category")
        val category: String="",
        @SerialName("description")
        val description: String="",
        @SerialName("dimensions")
        val dimensions: Dimensions? = null,
        @SerialName("discountPercentage")
        val discountPercentage: Double=0.0,
        @SerialName("id")
        val id: String="",
        @SerialName("images")
        val images: List<String> = emptyList(),
        @SerialName("meta")
        val meta: Meta? = null,
        @SerialName("minimumOrderQuantity")
        val minimumOrderQuantity: Int = 0,
        @SerialName("price")
        val price: String = "",
        @SerialName("rating")
        val rating: Double = 0.0,
        @SerialName("returnPolicy")
        val returnPolicy: String = "",
        @SerialName("reviews")
        val reviews: List<Review> = emptyList(),
        @SerialName("shippingInformation")
        val shippingInformation: String="",
        @SerialName("sku")
        val sku: String="",
        @SerialName("stock_quantity")
        val stock: String = "0",
        @SerialName("tags")
        val tags: List<String> = emptyList(),
        @SerialName("thumbnail")
        val thumbnail: String="",
        @SerialName("title")
        val title: String = "",
        @SerialName("warrantyInformation")
        val warrantyInformation: String="",
        @SerialName("weight")
        val weight: String="",
        @SerialName("created_at")
        val createdAt: String = "",
    ) {
        @Serializable
        data class Dimensions(
            @SerialName("depth")
            val depth: Double = 0.0,
            @SerialName("height")
            val height: Double = 0.0,
            @SerialName("width")
            val width: Double = 0.0
        )

        @Serializable
        data class Meta(
            @SerialName("barcode")
            val barcode: String = "",
            @SerialName("createdAt")
            val createdAt: String = "",
            @SerialName("qrCode")
            val qrCode: String = "",
            @SerialName("updatedAt")
            val updatedAt: String = ""
        )

        @Serializable
        data class Review(
            @SerialName("comment")
            val comment: String = "",
            @SerialName("date")
            val date: String = "",
            @SerialName("rating")
            val rating: Int = 0,
            @SerialName("reviewerEmail")
            val reviewerEmail: String = "",
            @SerialName("reviewerName")
            val reviewerName: String = ""
        )
    }
}