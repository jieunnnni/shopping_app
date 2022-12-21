package com.example.project_01_shopping_app.presentation.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.data.repository.user.UserRepository
import com.example.project_01_shopping_app.domain.BasketProductItemUseCase
import com.example.project_01_shopping_app.domain.GetProductItemUseCase
import com.example.project_01_shopping_app.presentation.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: Long,
    private val getProductItemUseCase: GetProductItemUseCase,
    private val basketProductItemUseCase: BasketProductItemUseCase,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private var _productDetailStateLiveData =
        MutableLiveData<ProductDetailState>(ProductDetailState.UnInitialized)
    val productDetailStateLiveData: LiveData<ProductDetailState> = _productDetailStateLiveData

    private lateinit var productEntity: ProductEntity

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ProductDetailState.Loading)
        getProductItemUseCase(productId)?.let { product ->
            productEntity = product
            setState(
                ProductDetailState.Success(product)
            )
        } ?: kotlin.run {
            setState(ProductDetailState.Error)
        }

        val isLiked = userRepository.getUserLikedProduct(productEntity.id) != null
        setState(
            ProductDetailState.Success(
                productEntity = productEntity,
                isLiked = isLiked
            )
        )

    }

    fun toGoBasket() = viewModelScope.launch {
        if (::productEntity.isInitialized) {
            val productId = basketProductItemUseCase(productEntity)
            if (productEntity.id == productId) {
                setState(ProductDetailState.Basket)
            }
        } else {
            setState(ProductDetailState.Error)
        }
    }

    fun toggleLikedProduct() = viewModelScope.launch {
        when (val data = productDetailStateLiveData.value) {
            is ProductDetailState.Success -> {
                userRepository.getUserLikedProduct(productEntity.id)?.let {
                    userRepository.deleteUserLikedProduct(it.id)
                    _productDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                    fetchData()
                } ?: kotlin.run {
                    userRepository.insertUserLikedProduct(productEntity)
                    _productDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                    fetchData()
                }
            }
            else -> Unit
        }
    }

    fun getProductInfo(): ProductEntity? {
        return when (val data = productDetailStateLiveData.value) {
            is ProductDetailState.Success -> {
                data.productEntity
            }
            else -> null
        }
    }

    private fun setState(state: ProductDetailState) {
        _productDetailStateLiveData.postValue(state)
    }
}