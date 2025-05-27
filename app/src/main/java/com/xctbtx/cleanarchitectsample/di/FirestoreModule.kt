package com.xctbtx.cleanarchitectsample.di

import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.impl.FireStoreApiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FireStoreModule {

    @Binds
    @Singleton
    abstract fun bindFireStoreApiService(
        impl: FireStoreApiServiceImpl
    ): FireStoreApiService
}