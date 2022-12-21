package com.example.project_01_shopping_app.presentation.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.project_01_shopping_app.data.preference.PreferenceManager
import com.example.project_01_shopping_app.data.repository.order.OrderRepository
import com.example.project_01_shopping_app.domain.DeleteOrderProductListUseCase
import com.example.project_01_shopping_app.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val preferenceManager: PreferenceManager,
    private val deleteOrderedProductListUseCase: DeleteOrderProductListUseCase
) : BaseViewModel() {

    private var _myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)
    val myLiveData: LiveData<MyState> = _myStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(MyState.Loading)
        preferenceManager.getIdToken()?.let {
            setState(
                MyState.Login(it)
            )
        } ?: kotlin.run {
            setState(
                MyState.Success.NotRegistered
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            setState(
                MyState.Success.Registered(
                    user.displayName ?: "익명",
                    user.photoUrl
                )
            )
        } ?: kotlin.run {
            setState(
                MyState.Success.NotRegistered
            )
        }
    }

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.removedToken()
        }
        deleteOrderedProductListUseCase()
        fetchData()
    }

    private fun setState(state: MyState) {
        _myStateLiveData.postValue(state)
    }
}