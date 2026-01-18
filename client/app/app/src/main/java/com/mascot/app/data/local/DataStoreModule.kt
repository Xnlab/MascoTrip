package com.mascot.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataStore 모듈
 * 
 * 역할:
 * - DataStore 인스턴스 제공
 * - UserPreferences 제공
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * DataStore 인스턴스 제공
     */
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    /**
     * UserPreferences 인스턴스 제공
     */
    @Provides
    @Singleton
    fun provideUserPreferences(
        dataStore: DataStore<Preferences>
    ): UserPreferences = UserPreferences(dataStore)
}
