package com.mascot.app.ui.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.local.UserPreferences
import com.mascot.app.data.remote.QuestApi
import com.mascot.app.data.tutorial.TutorialData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 튜토리얼 화면 ViewModel
 * 
 * 역할:
 * - 사용자 정보 입력 관리
 * - 맞춤형 퀘스트 생성 요청
 * - 사용자 ID 관리
 */
@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val questApi: QuestApi
) : ViewModel() {

    /** 사용자 ID */
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    /** 퀘스트 생성 중 상태 */
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    /** 에러 메시지 */
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUserId()
    }

    /**
     * 사용자 ID 로드 또는 생성
     */
    private fun loadUserId() {
        viewModelScope.launch {
            val existingUserId = userPreferences.userId.firstOrNull()
            if (existingUserId != null) {
                _userId.value = existingUserId
            } else {
                // 사용자 ID가 없으면 생성
                val newUserId = "user_${System.currentTimeMillis()}"
                userPreferences.saveUserId(newUserId)
                _userId.value = newUserId
            }
        }
    }

    /**
     * 맞춤형 퀘스트 생성 요청
     * 
     * @param tutorialData 사용자 입력 정보
     * @return 성공 여부
     */
    suspend fun generateQuests(tutorialData: TutorialData): Boolean {
        return try {
            _isGenerating.value = true
            _errorMessage.value = null
            
            questApi.generateQuestAll(tutorialData)
            true
        } catch (e: Exception) {
            _errorMessage.value = "퀘스트 생성에 실패했습니다: ${e.message}"
            false
        } finally {
            _isGenerating.value = false
        }
    }
}
