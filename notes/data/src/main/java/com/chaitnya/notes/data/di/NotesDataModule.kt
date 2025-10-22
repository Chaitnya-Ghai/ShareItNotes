package com.chaitnya.notes.data.di

import com.chaitnya.notes.data.repository.NotesRepoImpl
import com.chaitnya.notes.domain.repository.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesDataModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        val supabaseUrl = "https://klumnmagqsrzvahkvnyq.supabase.co"
        val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtsdW1ubWFncXNyenZhaGt2bnlxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzQ2MjY5MDgsImV4cCI6MjA1MDIwMjkwOH0.M5C64Cc7jIsISPiRqLyiEhxE4sZssmQYt14OJpKNGBM"
        return createSupabaseClient(supabaseUrl, supabaseKey) {
            install(Storage)
        }
    }

    @Provides
    fun provideRepoImpl(db: FirebaseFirestore , supabase: SupabaseClient): NotesRepository{
        return NotesRepoImpl(db,supabase)
    }
}