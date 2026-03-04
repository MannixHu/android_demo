package com.example.androidemo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CounterEntity::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao

    companion object {
        @Volatile
        private var INSTANCE: CounterDatabase? = null

        fun getDatabase(context: Context): CounterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CounterDatabase::class.java,
                    "counter_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
