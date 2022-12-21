package com.example.project_01_shopping_app.data.network

import com.example.project_01_shopping_app.data.response.ProductResponse
import com.example.project_01_shopping_app.data.response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>

    @GET("products/{productId}")
    suspend fun getProduct(@Path("productId") productId: Long): Response<ProductResponse>
}