package com.example.project_01_shopping_app.data.response

import com.example.project_01_shopping_app.data.entity.ProductEntity
import java.util.*

data class ProductResponse(
    val id: String,
    val createdAt: Long,
    val productName: String,
    val productPrice: Int,
    val productImage: String,
    val productType: String,
    val productIntroductionImage: String
) {
    fun toEntity(): ProductEntity =
        ProductEntity(
            id = id.toLong(),
            createdAt = Date(createdAt),
            productName = productName,
            productPrice = productPrice.toDouble().toInt(),
            productImage = productImage,
            productType = productType,
            productIntroductionImage = productIntroductionImage
        )
}
