package com.example.project_01_shopping_app.widget

import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.databinding.ViewholderProductItemBinding
import com.example.project_01_shopping_app.extensions.loadCenterCrop
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductItemViewHolder>() {

    private var productList: List<ProductEntity> = listOf()
    private lateinit var productItemClickListener: (ProductEntity) -> Unit

    inner class ProductItemViewHolder(
        private val binding: ViewholderProductItemBinding,
        val productItemClickListener: (ProductEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: ProductEntity) = with(binding) {
            productNameTextView.text = data.productName
            productImageView.loadCenterCrop(data.productImage, 8f)
            productPriceTextView.text = "${data.productPrice}원"
        }

        fun bindViews(data: ProductEntity) {
            binding.root.setOnClickListener {
                productItemClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = ViewholderProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemViewHolder(view, productItemClickListener,)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bindData(productList[position])
        holder.bindViews(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(productList: List<ProductEntity>, productItemClickListener: (ProductEntity) -> Unit = { }) {
        this.productList = productList
        this.productItemClickListener = productItemClickListener
        notifyDataSetChanged()
    }
}