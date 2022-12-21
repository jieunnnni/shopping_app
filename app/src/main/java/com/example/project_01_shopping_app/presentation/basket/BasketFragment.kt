package com.example.project_01_shopping_app.presentation.basket

import android.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.project_01_shopping_app.databinding.FragmentBasketBinding
import com.example.project_01_shopping_app.extensions.toast
import com.example.project_01_shopping_app.presentation.base.BaseFragment
import com.example.project_01_shopping_app.presentation.home.detail.ProductDetailActivity
import com.example.project_01_shopping_app.presentation.main.MainActivity
import com.example.project_01_shopping_app.widget.ProductListAdapter
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class BasketFragment: BaseFragment<BasketViewModel, FragmentBasketBinding>() {

    companion object {
        fun newInstance() = BasketFragment()

        const val TAG = "BasketFragment"
    }

    override fun getViewBinding(): FragmentBasketBinding = FragmentBasketBinding.inflate(layoutInflater)

    override val viewModel by viewModel <BasketViewModel>()

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val adapter = ProductListAdapter()

    override fun observeData() = viewModel.basketStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is BasketState.Uninitialized -> initViews(binding)
            is BasketState.Loading -> handleLoadingState()
            is BasketState.Success -> handleSuccessState(it)
            is BasketState.Order -> handleOrderState()
            is BasketState.Error -> handleErrorState()
        }
    }

    private fun initViews(binding: FragmentBasketBinding) = with(binding) {
        recyclerView.adapter = adapter

        allClearButton.setOnClickListener {
            viewModel.clearBasketProducts()
        }
        orderButton.setOnClickListener {
            if (firebaseAuth.currentUser == null) {
                alertLoginNeed {
                    (requireActivity() as MainActivity).goToTab(MainActivity.MainTabMenu.MY_PAGE)
                }
            } else {
                viewModel.orderProducts()
            }
            alertOrderedMessage {
                (requireActivity() as MainActivity).goToTab(MainActivity.MainTabMenu.MY_PAGE)
            }
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccessState(state: BasketState.Success) = with(binding) {
        progressBar.isGone = true
        adapter.setProductList(state.productList) {
            startActivity(
                ProductDetailActivity.newIntent(requireContext(), it.id)
            )
        }
        val basketIsEmpty = state.productList.isNullOrEmpty()
        orderButton.isEnabled = basketIsEmpty.not()
    }

    private fun handleOrderState() {
        requireContext().toast("성공적으로 주문을 완료하였습니다.")
    }

    private fun handleErrorState() {
        requireContext().toast("에러가 발생했습니다.")
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("로그인이 필요합니다.")
            .setMessage("주문하려면 로그인이 필요합니다. 로그인 창으로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun alertOrderedMessage(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("주문 완료")
            .setMessage("성공적으로 주문을 완료하셨습니다. 마이페이지로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }
}