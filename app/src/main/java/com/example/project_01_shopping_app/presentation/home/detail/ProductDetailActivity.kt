package com.example.project_01_shopping_app.presentation.home.detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.project_01_shopping_app.R
import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.databinding.ActivityProductDetailBinding
import com.example.project_01_shopping_app.extensions.load
import com.example.project_01_shopping_app.extensions.loadCenterCrop
import com.example.project_01_shopping_app.extensions.toast
import com.example.project_01_shopping_app.presentation.base.BaseActivity
import com.example.project_01_shopping_app.presentation.base.BaseFragment
import com.example.project_01_shopping_app.presentation.basket.BasketFragment
import com.example.project_01_shopping_app.presentation.home.ProductListFragment
import com.example.project_01_shopping_app.presentation.main.MainActivity
import com.example.project_01_shopping_app.util.event.MenuChangeEventBus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductDetailActivity: BaseActivity<ProductDetailViewModel, ActivityProductDetailBinding>() {

    companion object {
        const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"
        const val PRODUCT_BASKET_RESULT_CODE = 99

        fun newIntent(context: Context, productId: Long) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, productId)
            }
    }

    override val viewModel by viewModel <ProductDetailViewModel> {
        parametersOf(
            intent.getLongExtra(PRODUCT_ID_KEY, -1)
        )
    }

    override fun getViewBinding(): ActivityProductDetailBinding =
        ActivityProductDetailBinding.inflate(layoutInflater)

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun observeData() = viewModel.productDetailStateLiveData.observe(this){
        when (it) {
            is ProductDetailState.UnInitialized -> initViews()
            is ProductDetailState.Loading -> handleLoading()
            is ProductDetailState.Success -> handleSuccess(it)
            is ProductDetailState.Error -> handleError()
            is ProductDetailState.Basket -> handleBasket()
        }
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }

        toGoBasketButton.setOnClickListener {
            if (firebaseAuth.currentUser == null) {
                alertLoginNeed {
                    lifecycleScope.launch {
                        menuChangeEventBus.changeMenu(MainActivity.MainTabMenu.MY_PAGE)
                        finish()
                    }
                }
            } else {
                viewModel.toGoBasket()
            }
        }

        likeButton.setOnClickListener {
            if (firebaseAuth.currentUser == null) {
                alertLoginNeed {
                    lifecycleScope.launch {
                        menuChangeEventBus.changeMenu(MainActivity.MainTabMenu.MY_PAGE)
                        finish()
                    }
                }
            } else {
                viewModel.toggleLikedProduct()
            }
        }

        shareButton.setOnClickListener {
            viewModel.getProductInfo()?.let { productInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "?????? : ${productInfo.productName}" +
                                "\n ???????????? : ${productInfo.productType}" +
                                "\n?????? : ${productInfo.productPrice}"
                    )
                    Intent.createChooser(this, "????????????")
                }
                startActivity(intent)
            }
        }


    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    @SuppressLint("SetTextI18n")
    private fun handleSuccess(state: ProductDetailState.Success) = with(binding) {
        progressBar.isGone = true

        val product = state.productEntity
        title = product.productName
        productCategoryTextView.text = product.productType
        productImageView.loadCenterCrop(product.productImage, 8f)
        productPriceTextView.text = "${product.productPrice}???"
        introductionImageView.load(state.productEntity.productImage)

        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this@ProductDetailActivity, if (state.isLiked == true) {
                R.drawable.ic_enabled_heart
            } else {
                R.drawable.ic_disabled_heart
            }),
            null, null, null
        )
    }

    private fun handleError() {
        toast("?????? ????????? ????????? ??? ????????????.")
        finish()
    }

    private fun handleBasket() {
        setResult(PRODUCT_BASKET_RESULT_CODE)
        toast("??????????????? ???????????? ????????? ???????????????.")
        finish()
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("???????????? ???????????????.")
            .setMessage("???????????? ????????? ???????????????. ????????? ????????? ?????????????????????????")
            .setPositiveButton("??????") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("??????") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}