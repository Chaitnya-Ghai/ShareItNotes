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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
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
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onEmailChange(email: String){
        _email.update { email }
    }
    fun onPasswordChange(password: String){
        _password.update{ password }
    }
    fun onNameChange(name: String){
        _name.update { name }
    }
    private val _isError = MutableSharedFlow<String?>()
    val isError :SharedFlow<String?> = _isError

    fun login() {
        logInUseCase(email.value, password.value).onStart { _isLoading.update { true } }
            .onEach { result ->
                result.onSuccess { data ->
                    _uiState.update { it.copy(navigateToNotesNavGraph = true) }
                    _isLoading.update { false }
                }.onFailure { error ->
                    _isLoading.update { false }
                    _isError.emit(error.message.toString())
                }
            }.catch {
                _isLoading.update { false }
            }
            .onCompletion {
                _isLoading.update { false }
                _uiState.update { it.copy(navigateToNotesNavGraph = true) }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun register() {
        registerUseCase(email.value, password.value).onStart { _isLoading.update { true } }
            .onEach { result ->
                _isLoading.update { false }
                result.onSuccess { data ->
                    _uiState.update { it.copy(navigateToNotesNavGraph = true) }
                }.onFailure { error ->
                    _isLoading.update { false }
                    _isError.emit(error.message.toString())
                }
            }.catch {
                _isLoading.update { false }
            }
            .onCompletion {
                _isLoading.update { false }
                _uiState.update { it.copy(navigateToNotesNavGraph = true) }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}