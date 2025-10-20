package com.chaitnya.auth.domain.use_cases

import com.chaitnya.auth.domain.repoistory.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String) = authRepository.register(email, password)
}