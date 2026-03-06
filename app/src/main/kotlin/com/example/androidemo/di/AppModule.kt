package com.example.androidemo.di

import android.content.Context
import com.example.androidemo.data.datasource.CounterLocalDataSource
import com.example.androidemo.data.remote.UpdateService
import com.example.androidemo.data.repository.CounterRepositoryImpl
import com.example.androidemo.domain.repository.CounterRepository
import com.example.androidemo.domain.usecase.CheckUpdateUseCase
import com.example.androidemo.domain.usecase.DecrementCounterUseCase
import com.example.androidemo.domain.usecase.GetCounterUseCase
import com.example.androidemo.domain.usecase.IncrementCounterUseCase
import com.example.androidemo.domain.usecase.ResetCounterUseCase
import com.example.androidemo.util.AppVersionProvider
import com.example.androidemo.util.UpdateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUpdateService(retrofit: Retrofit): UpdateService {
        return retrofit.create(UpdateService::class.java)
    }

    @Provides
    @Singleton
    fun provideUpdateManager(
        @ApplicationContext context: Context,
        updateService: UpdateService
    ): UpdateManager {
        return UpdateManager(context, updateService)
    }

    @Provides
    @Singleton
    fun provideCheckUpdateUseCase(updateManager: UpdateManager): CheckUpdateUseCase {
        return CheckUpdateUseCase(updateManager)
    }

    @Provides
    @Singleton
    fun provideAppVersionProvider(@ApplicationContext context: Context): AppVersionProvider {
        return AppVersionProvider(context)
    }
}
