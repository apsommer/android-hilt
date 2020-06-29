package com.example.android.hilt.di

import com.example.android.hilt.data.LoggerDataSource
import com.example.android.hilt.data.LoggerInMemoryDataSource
import com.example.android.hilt.data.LoggerLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Qualifier
import javax.inject.Singleton

// this file shows two different interface implementations of the same object
// this approach requires the @Qualifier keyword so Hilt knows which implementation to inject

@Qualifier
annotation class InMemoryLogger

@Qualifier
annotation class DatabaseLogger

// this app-level module could be injected into LogApplication class, but can also be separated in this
// way for clarity. Conversely, @InMemoryLogger is installed in ActivityComponent and therefore can
// not be injected at the app-level since each level has no reference to child bindings
@InstallIn(ApplicationComponent::class) // app-level access
@Module
abstract class LoggingDatabaseModule {

    // @Binds methods must have the scoping annotations if the type is scoped!
    // These scoping annotations are not needed in the class files themselves

    @DatabaseLogger // custom qualifier
    @Singleton // db data source is singleton at app-level
    @Binds // keyword used for interface implementations
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource) : LoggerDataSource
}

@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @InMemoryLogger // custom qualifier
    @ActivityScoped // local logger is singleton at activity-level
    @Binds // keyword used for interface implementations
    abstract fun findInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}