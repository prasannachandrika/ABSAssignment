package com.example.absassignment.di

import com.example.absassignment.domainn.FetchUsersUseCase
import com.example.absassignment.data.remote.ApiService
import com.example.absassignment.domainn.UserRepository
import com.example.absassignment.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }
    @Provides
    fun provideFetchUsersUseCase(userRepository: UserRepositoryImpl ): FetchUsersUseCase {
        return FetchUsersUseCase(userRepository)
    }
}
