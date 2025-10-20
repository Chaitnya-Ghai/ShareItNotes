package com.chaitnya.auth.domain.use_cases

import com.chaitnya.auth.domain.repoistory.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.getCurrentUser()
}