package com.example.project_01_shopping_app.di

import com.example.project_01_shopping_app.data.db.provideDB
import com.example.project_01_shopping_app.data.db.provideToDoDao
import com.example.project_01_shopping_app.data.network.buildOkHttpClient
import com.example.project_01_shopping_app.data.network.provideGsonConverterFactory
import com.example.project_01_shopping_app.data.network.provideProductApiService
import com.example.project_01_shopping_app.data.network.provideProductRetrofit
import com.example.project_01_shopping_app.data.preference.PreferenceManager
import com.example.project_01_shopping_app.data.repository.order.DefaultOrderRepository
import com.example.project_01_shopping_app.data.repository.order.OrderRepository
import com.example.project_01_shopping_app.data.repository.product.DefaultProductRepository
import com.example.project_01_shopping_app.data.repository.product.ProductRepository
import com.example.project_01_shopping_app.data.repository.user.DefaultUserRepository
import com.example.project_01_shopping_app.data.repository.user.UserRepository
import com.example.project_01_shopping_app.domain.*
import com.example.project_01_shopping_app.presentation.basket.BasketViewModel
import com.example.project_01_shopping_app.presentation.home.ProductListViewModel
import com.example.project_01_shopping_app.presentation.home.detail.ProductDetailViewModel
import com.example.project_01_shopping_app.presentation.main.MainViewModel
import com.example.project_01_shopping_app.presentation.my.MyViewModel
import com.example.project_01_shopping_app.util.event.MenuChangeEventBus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Coroutines Dispatcher
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // UseCase
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { BasketProductItemUseCase(get()) }
    factory { GetAllProductListInBasketUseCase(get()) }
    factory { ClearBasketProductsUseCase(get()) }
    factory { DeleteOrderProductListUseCase(get()) }

    // Repository
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get()) }

    // ProvideAPI
    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }

    single { PreferenceManager(androidContext()) }

    // ViewModel
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get(), get(), get()) }
    viewModel { BasketViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get(), get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }

    // Firebase
    single { Firebase.firestore }

    single { MenuChangeEventBus() }
}