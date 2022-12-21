package com.example.project_01_shopping_app.data.repository.order

import com.example.project_01_shopping_app.data.entity.OrderEntity
import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class DefaultOrderRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val fireStore: FirebaseFirestore
) : OrderRepository {
    override suspend fun orderProduct(
        userId: String,
        productId: Long,
        productList: List<ProductEntity>
    ): Result = withContext(ioDispatcher) {
        val result: Result
        val orderBasketData = hashMapOf(
            "productId" to productId,
            "userId" to userId,
            "orderProductList" to productList
        )
        result = try {
            fireStore
                .collection("order")
                .add(orderBasketData)
            Result.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
        return@withContext result
    }

    sealed class Result {

        data class Success<T>(
            val data: T? = null
        ) : Result()

        data class Error(
            val e: Throwable
        ) : Result()
    }
}