package com.chaitnya.auth.domain.use_cases

import com.chaitnya.auth.domain.repoistory.AuthRepository

class LogInUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String) = authRepository.login(email,password)
}