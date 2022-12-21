package com.example.project_01_shopping_app.presentation.my

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.project_01_shopping_app.R
import com.example.project_01_shopping_app.databinding.FragmentMyBinding
import com.example.project_01_shopping_app.extensions.loadCenterCrop
import com.example.project_01_shopping_app.extensions.toast
import com.example.project_01_shopping_app.presentation.base.BaseFragment
import com.example.project_01_shopping_app.presentation.home.detail.ProductDetailActivity
import com.example.project_01_shopping_app.widget.ProductListAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    companion object {
        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    override val viewModel by viewModel <MyViewModel>()

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    //Log.e(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    viewModel.saveToken(account.idToken ?: throw Exception())
                } ?: throw Exception()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var isFirstShown = false

    // TODO 무슨 작용하는지 확인해보기
//    override fun onResume() {
//        super.onResume()
//        if (isFirstShown.not()) {
//            isFirstShown = true
//        } else {
//            viewModel.fetchData()
//        }
//    }

    override fun observeData() = viewModel.myLiveData.observe(viewLifecycleOwner) {
        when(it) {
            is MyState.Uninitialized -> initViews()
            is MyState.Loading -> handleLoadingState()
            is MyState.Login -> handleLoginState(it)
            is MyState.Success -> handleSuccessState(it)
            is MyState.Error -> handleErrorState(it)
        }

    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener { signInGoogle() }

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
        loginRequiredGroup.isGone = true
    }

    private fun handleLoginState(state: MyState.Login) = with(binding) {
        progressBar.isVisible = true

        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                    requireContext().toast("로그아웃 되었습니다. 재로그인이 필요합니다.")
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.isGone = true
        when (state) {
            is MyState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.loadCenterCrop(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

//        if (state.orderedProductList.isEmpty()) {
//            emptyResultTextView.isGone = false
//            recyclerView.isGone = true
//        } else {
//            emptyResultTextView.isGone = true
//            recyclerView.isGone = false
//        }
    }

    private fun handleErrorState(state: MyState.Error) {
        Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
    }

    private fun signInGoogle() {
        val signInIntent: Intent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }
}