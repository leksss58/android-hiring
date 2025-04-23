package com.example.myapplication.di

import com.example.myapplication.data.mapper.TestMapper
import com.example.myapplication.ui.mapper.UiTestMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {
    @Provides
    @Singleton
    fun provideTestMapper(): TestMapper = TestMapper()

    @Provides
    @Singleton
    fun provideUiTestMapper(): UiTestMapper = UiTestMapper()
}