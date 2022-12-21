package com.example.project_01_shopping_app.presentation.my

import android.net.Uri
import androidx.annotation.StringRes
import androidx.room.Index
import com.example.project_01_shopping_app.data.entity.OrderEntity
import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.data.repository.order.DefaultOrderRepository
import com.example.project_01_shopping_app.presentation.basket.BasketState

sealed class MyState {

    object Uninitialized: MyState()

    object Loading: MyState()

    data class Login(
        val idToken: String
    ): MyState()

    sealed class Success: MyState() {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?
        ): Success()

        object NotRegistered: Success()
    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyState()
}