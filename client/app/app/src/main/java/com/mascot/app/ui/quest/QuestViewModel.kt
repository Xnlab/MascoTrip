package com.mascot.app.ui.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.local.UserPreferences
import com.mascot.app.data.model.QuestItem
import com.mascot.app.data.repository.QuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 퀘스트 화면 ViewModel
 * 
 * 역할:
 * - 퀘스트 목록 로드 및 관리
 * - 완료된 퀘스트 추적
 * - 로딩 상태 관리
 */
@HiltViewModel
class QuestViewModel @Inject constructor(
    private val repository: QuestRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    /** 지역별 퀘스트 목록 */
    private val _quests = MutableStateFlow<Map<String, List<QuestItem>>>(emptyMap())
    val quests: StateFlow<Map<String, List<QuestItem>>> = _quests

    /** 완료된 퀘스트 목록 */
    private val _completedQuests = MutableStateFlow<List<QuestItem>>(emptyList())
    val completedQuests: StateFlow<List<QuestItem>> = _completedQuests

    /** 로딩 상태 */
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        loadAllQuests()
    }

    /**
     * 모든 지역의 퀘스트 목록 로드
     * 
     * 사용자 ID는 DataStore에서 가져옴
     */
    private fun loadAllQuests() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // DataStore에서 사용자 ID 가져오기
                val userId = userPreferences.userId.firstOrNull() ?: "default-user"
                _quests.value = repository.getAllQuests(userId)
            } catch (e: Exception) {
                // 에러 발생 시 빈 맵 유지 (UI에서 빈 상태 표시)
                _quests.value = emptyMap()
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * 퀘스트 완료 처리
     * 
     * @param quest 완료한 퀘스트
     */
    fun completeQuest(quest: QuestItem) {
        _completedQuests.value = _completedQuests.value + quest
    }
}
