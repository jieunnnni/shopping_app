package com.example.project_01_shopping_app.presentation.home

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.example.project_01_shopping_app.databinding.FragmentProductListBinding
import com.example.project_01_shopping_app.presentation.base.BaseFragment
import com.example.project_01_shopping_app.presentation.home.detail.ProductDetailActivity
import com.example.project_01_shopping_app.presentation.main.MainActivity
import com.example.project_01_shopping_app.widget.ProductListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    companion object {
        fun newInstance() = ProductListFragment()

        const val TAG = "ProductListFragment"
    }

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    override val viewModel by viewModel <ProductListViewModel>()

    private val adapter = ProductListAdapter()

    private val startProductDetailForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == ProductDetailActivity.PRODUCT_BASKET_RESULT_CODE) {
                (requireActivity() as MainActivity).viewModel.refreshBasketList()
            }
        }

    override fun observeData() {
        viewModel.productListStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is  ProductListState.UnInitialized -> {
                    initViews(binding)
                }
                is ProductListState.Loading -> {
                    handleLoadingState()
                }
                is ProductListState.Success -> {
                    handleSuccessState(it)
                }
                is ProductListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun initViews(binding: FragmentProductListBinding) = with(binding) {
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ProductListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.productList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.productList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setProductList(state.productList) {
                startProductDetailForResult.launch(
                    ProductDetailActivity.newIntent(requireContext(), it.id)
                )
            }
        }
    }

    private fun handleErrorState() {
        requireContext().toast("에러가 발생했습니다.")
    }
}
