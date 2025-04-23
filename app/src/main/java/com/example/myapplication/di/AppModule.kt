package com.example.myapplication.di

import android.app.Application
import android.content.Context
import com.example.myapplication.MyApp
import com.example.myapplication.providers.SharedPreferencesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApplication(app: MyApp): Application = app

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesProvider =
        SharedPreferencesProvider(context)
}