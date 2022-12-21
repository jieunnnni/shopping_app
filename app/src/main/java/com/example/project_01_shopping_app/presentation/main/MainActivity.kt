package com.example.project_01_shopping_app.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.project_01_shopping_app.R
import com.example.project_01_shopping_app.databinding.ActivityMainBinding
import com.example.project_01_shopping_app.presentation.base.BaseActivity
import com.example.project_01_shopping_app.presentation.base.BaseFragment
import com.example.project_01_shopping_app.presentation.basket.BasketFragment
import com.example.project_01_shopping_app.presentation.home.ProductListFragment
import com.example.project_01_shopping_app.presentation.my.MyFragment
import com.example.project_01_shopping_app.util.event.MenuChangeEventBus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), BottomNavigationView.OnNavigationItemSelectedListener {

    override val viewModel by  viewModel<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect {
                goToTab(it)
            }
        }
        initViews()

    }

    private fun initViews() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(ProductListFragment(), ProductListFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(ProductListFragment.newInstance(), ProductListFragment.TAG)
                true
            }
            R.id.menu_basket -> {
                showFragment(BasketFragment.newInstance(), BasketFragment.TAG)
                true
            }
            R.id.menu_my_page -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        when (it) {
            is MainState.RefreshBasketList -> {
                binding.bottomNav.selectedItemId = R.id.menu_basket
                val fragment = supportFragmentManager.findFragmentByTag(BasketFragment.TAG)
                (fragment as? BaseFragment<*, *>)?.viewModel?.fetchData()
            }
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu) {
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }

    enum class MainTabMenu(@IdRes val menuId: Int) {
        MY_PAGE(R.id.menu_my_page)
    }
}