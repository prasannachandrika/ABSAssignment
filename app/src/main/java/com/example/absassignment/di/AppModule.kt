package com.example.absassignment.di

import com.example.absassignment.domainn.FetchUsersUseCase
import com.example.absassignment.data.remote.api.ApiService
import com.example.absassignment.data.remote.source.RemoteDataSource
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
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource): UserRepository {
        return UserRepositoryImpl(remoteDataSource)
    }

    @Provides
    fun provideFetchUsersUseCase(userRepository: UserRepositoryImpl ): FetchUsersUseCase {
        return FetchUsersUseCase(userRepository)
    }
}
