package com.example.project_01_shopping_app.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.project_01_shopping_app.presentation.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel: BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch {

    }

    private var _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData: LiveData<MainState> = _mainStateLiveData

    fun refreshBasketList() = viewModelScope.launch {
        _mainStateLiveData.postValue(MainState.RefreshBasketList)
    }

}