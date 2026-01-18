package com.mascot.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 사용자 설정을 관리하는 DataStore 키 정의
 */
object UserPreferencesKeys {
    val USER_ID = stringPreferencesKey("user_id")
}

/**
 * 사용자 설정 관리 Repository
 * 
 * 역할:
 * - 사용자 ID 저장 및 조회
 * - DataStore를 통한 영구 저장
 */
@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * 현재 사용자 ID 조회
     * 
     * @return 사용자 ID Flow (없으면 null)
     */
    val userId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.USER_ID]
    }

    /**
     * 사용자 ID 저장
     * 
     * @param userId 저장할 사용자 ID
     */
    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USER_ID] = userId
        }
    }

    /**
     * 사용자 ID 가져오기 (동기적, 초기값 필요 시 사용)
     * 
     * @return 사용자 ID (없으면 기본값 "default-user")
     */
    suspend fun getUserIdOrDefault(): String {
        return dataStore.data.map { preferences ->
            preferences[UserPreferencesKeys.USER_ID] ?: "default-user"
        }.collect { return@collect it }
    }
}
