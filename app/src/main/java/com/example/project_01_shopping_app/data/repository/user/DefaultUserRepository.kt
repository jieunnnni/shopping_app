package com.example.project_01_shopping_app.data.repository.user

import com.example.project_01_shopping_app.data.db.dao.ProductDao
import com.example.project_01_shopping_app.data.entity.ProductEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val productDao: ProductDao,
    private val ioDispatcher: CoroutineDispatcher
): UserRepository {

    override suspend fun getUserLikedProduct(id: Long): ProductEntity? = withContext(ioDispatcher) {
        productDao.getById(id)
    }

    override suspend fun getAllUserLikedProduct(): List<ProductEntity> = withContext(ioDispatcher) {
        productDao.getAll()
    }

    override suspend fun insertUserLikedProduct(productEntity: ProductEntity): Long = withContext(ioDispatcher) {
        productDao.insert(productEntity)
    }

    override suspend fun deleteUserLikedProduct(id: Long) = withContext(ioDispatcher) {
        productDao.delete(id)
    }

    override suspend fun deleteAllUserLikedProduct() = withContext(ioDispatcher) {
        productDao.deleteAll()
    }
}