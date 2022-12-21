package com.example.project_01_shopping_app.presentation.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.project_01_shopping_app.R
import com.example.project_01_shopping_app.data.repository.order.DefaultOrderRepository
import com.example.project_01_shopping_app.data.repository.order.OrderRepository
import com.example.project_01_shopping_app.domain.ClearBasketProductsUseCase
import com.example.project_01_shopping_app.domain.GetAllProductListInBasketUseCase
import com.example.project_01_shopping_app.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BasketViewModel(
    private val getAllProductListInBasketUseCase: GetAllProductListInBasketUseCase,
    private val clearBasketProductsUseCase: ClearBasketProductsUseCase,
    private val orderRepository: OrderRepository
): BaseViewModel() {

    private var _basketStateLiveData = MutableLiveData<BasketState>(BasketState.Uninitialized)
    val basketStateLiveData: LiveData<BasketState> = _basketStateLiveData

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            BasketState.Success(
                getAllProductListInBasketUseCase()
            )
        )
    }

    fun clearBasketProducts() = viewModelScope.launch {
        clearBasketProductsUseCase()
        fetchData()
    }

    fun orderProducts() = viewModelScope.launch {
        val productList = getAllProductListInBasketUseCase()
        if (productList.isNotEmpty()) {
            val productId = productList.first().id
            firebaseAuth.currentUser?.let { user ->
                when (val data = orderRepository.orderProduct(user.uid, productId, productList)) {
                    is DefaultOrderRepository.Result.Success<*> -> {
                        setState(
                            BasketState.Order
                        )
                        clearBasketProductsUseCase()
                        fetchData()
                    }
                    is DefaultOrderRepository.Result.Error -> {
                        setState(
                            BasketState.Error(
                                R.string.request_error, data.e
                            )
                        )
                    }
                }
            } ?: kotlin.run {
                setState(
                    BasketState.Error(
                        R.string.user_id_not_found, IllegalArgumentException()
                    )
                )
            }
        }
    }

    private fun setState(state: BasketState) {
        _basketStateLiveData.postValue(state)
    }
}