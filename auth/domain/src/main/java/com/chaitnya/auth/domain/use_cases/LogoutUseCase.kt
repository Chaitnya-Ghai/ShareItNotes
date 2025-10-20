package com.chaitnya.auth.domain.use_cases

import com.chaitnya.auth.domain.repoistory.AuthRepository

class LogoutUseCase( private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.logout()
}