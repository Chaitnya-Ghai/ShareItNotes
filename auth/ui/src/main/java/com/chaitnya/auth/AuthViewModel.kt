package com.chaitnya.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.chaitnya.auth.domain.use_cases.GetCurrentUserUseCase
import com.chaitnya.auth.domain.use_cases.LogInUseCase
import com.chaitnya.auth.domain.use_cases.LogoutUseCase
import com.chaitnya.auth.domain.use_cases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    private val _email = MutableStateFlow("")
    val email get() = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password get() = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name get() = _name.asStateFlow()

    private val _isLogin = MutableStateFlow(true)
    val isLogin get() = _name.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun onEmailChange(email: String){
        _email.update { email }
    }
    fun onPasswordChange(password: String){
        _password.update{ password }
    }
    fun onNameChange(name: String){
        _name.update { name }
    }
    fun onToggleChange(){
        _isLogin.update { it.not() }
    }


    fun login() {
        logInUseCase(email.value, password.value).onStart { _isLoading.update { true } }
            .onEach { result ->
                result.onSuccess { data ->
                    _isLoading.update { false }
                }.onFailure { error ->
                }
            }.catch { _isLoading.update { false } }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun register() {
        registerUseCase(email.value, password.value).onStart { _isLoading.update { true } }
            .onEach { result ->
                _isLoading.update { false }
                result.onSuccess { data ->

                }.onFailure { error ->

                }
            }.catch { _isLoading.update { false } }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}