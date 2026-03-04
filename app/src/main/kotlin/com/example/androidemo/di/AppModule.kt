package com.example.androidemo.di

import com.example.androidemo.data.datasource.CounterLocalDataSource
import com.example.androidemo.data.repository.CounterRepositoryImpl
import com.example.androidemo.domain.repository.CounterRepository
import com.example.androidemo.domain.usecase.DecrementCounterUseCase
import com.example.androidemo.domain.usecase.GetCounterUseCase
import com.example.androidemo.domain.usecase.IncrementCounterUseCase
import com.example.androidemo.domain.usecase.ResetCounterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCounterLocalDataSource(
        counterDao: com.example.androidemo.data.local.CounterDao
    ): CounterLocalDataSource {
        return CounterLocalDataSource(counterDao)
    }

    @Provides
    @Singleton
    fun provideCounterRepository(
        counterLocalDataSource: CounterLocalDataSource
    ): CounterRepository {
        return CounterRepositoryImpl(counterLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideGetCounterUseCase(repository: CounterRepository): GetCounterUseCase {
        return GetCounterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideIncrementCounterUseCase(repository: CounterRepository): IncrementCounterUseCase {
        return IncrementCounterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDecrementCounterUseCase(repository: CounterRepository): DecrementCounterUseCase {
        return DecrementCounterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideResetCounterUseCase(repository: CounterRepository): ResetCounterUseCase {
        return ResetCounterUseCase(repository)
    }
}
