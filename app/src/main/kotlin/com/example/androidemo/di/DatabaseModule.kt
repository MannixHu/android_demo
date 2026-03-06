package com.example.androidemo.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidemo.data.local.CounterDatabase
import com.example.androidemo.data.local.CounterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCounterDatabase(
        @ApplicationContext context: Context
    ): CounterDatabase {
        return Room.databaseBuilder(
            context,
            CounterDatabase::class.java,
            "counter_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Initialize default counter value on first launch
                    db.execSQL(
                        "INSERT INTO counter_table (id, value, timestamp) " +
                        "VALUES (1, 0, ${System.currentTimeMillis()})"
                    )
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideCounterDao(database: CounterDatabase): CounterDao {
        return database.counterDao()
    }
}
