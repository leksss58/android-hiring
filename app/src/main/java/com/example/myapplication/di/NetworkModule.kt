package com.example.myapplication.di

import com.example.myapplication.data.mapper.TestMapper
import com.example.myapplication.data.socketmanager.SocketManager
import com.example.myapplication.domain.SocketRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    @Named("address")
    fun provideAddress(): String = "challenge.ciliz.com"

    @Provides
    @Singleton
    @Named("port")
    fun providePort(): Int = 2222

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSocketRepository(
        @Named("address") address: String,
        @Named("port") port: Int,
        gson: Gson,
        testMapper: TestMapper
    ): SocketRepository {
        return SocketManager(
            address = address,
            port = port,
            gson = gson,
            testMapper = testMapper
        )
    }
}