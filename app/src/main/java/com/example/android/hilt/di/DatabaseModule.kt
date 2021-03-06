package com.example.android.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

// @InstallIn defines the container scope, and @Module defines the bindings in this scope
@InstallIn(ApplicationComponent::class)
@Module // for Kotlin modules that only contain @Provides functions can be object classes.
object DatabaseModule {

    @Provides // defines objects that can not be constructor injected, such as LogDao
    fun provideLogDao(database: AppDatabase): LogDao { // function parameters define this object's transitive dependencies
        return database.logDao()
    }

    // AppDatabase is another class this project does not own - it is generated by Room, so same idea: provide it
    @Provides
    @Singleton // we always want the same database instance
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        // @ApplicationContext is a default binding provided by Hilt for use in custom binding containers (this)
        // since the context here must be app-level this default binding is used
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }

}