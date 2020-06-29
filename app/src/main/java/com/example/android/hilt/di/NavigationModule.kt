package com.example.android.hilt.di

import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class) // activity level container
@Module // hilt dependency container
abstract class NavigationModule {

    @Binds // @Binds is used for interfaces, the return type is the interface, the function is passed an implementation
    // this function has not been scoped to this activity container (using Singleton), a new object will be returned on each call
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}