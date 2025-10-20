package com.chaitnya.auth.data.repository

import com.chaitnya.auth.domain.model.User
import com.chaitnya.auth.domain.repoistory.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class AuthRepoImpl(
    private val firebaseAuth : FirebaseAuth
) : AuthRepository {
    override fun login(
        email: String,
        password: String,
    ): Flow<Result<User>> {
        return callbackFlow {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { firebaseUser ->
                    firebaseUser.user?.let {
                        trySend(
                            Result.success(
                                User(
                                    uid = it.uid,
                                    email = it.email.orEmpty(),
                                    displayName = it.displayName.orEmpty()
                                )
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    trySend(Result.failure(it))
                }
            awaitClose {  }
        }.flowOn(Dispatchers.IO)
    }

    override fun register(
        email: String,
        password: String,
    ): Flow<Result<User>> = callbackFlow {
        val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
        task.addOnSuccessListener { firebaseUser ->
            firebaseUser.user?.let {
                trySend(
                    Result.success(
                        User(
                            uid = it.uid,
                            email = it.email.orEmpty(),
                            displayName = it.displayName.orEmpty()
                        )
                    )
                )
            }
        }.addOnFailureListener { exception ->
            trySend(Result.failure(exception))
        }

        awaitClose { }
    }.flowOn(Dispatchers.IO)


    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.let {
            User(
                uid = it.uid,
                email = it.email.orEmpty(),
                displayName = it.displayName.orEmpty()
            )
        }
    }
}