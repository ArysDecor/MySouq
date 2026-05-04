package com.example.mysouq.di

import com.example.mysouq.data.repository.CartRepositoryImpl
import com.example.mysouq.data.repository.FavoriteRepositoryImpl
import com.example.mysouq.data.repository.ProductRepositoryImpl
import com.example.mysouq.data.repository.UserPreferencesRepositoryImpl
import com.example.mysouq.domain.repository.CartRepository
import com.example.mysouq.domain.repository.FavoriteRepository
import com.example.mysouq.domain.repository.ProductRepository
import com.example.mysouq.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
