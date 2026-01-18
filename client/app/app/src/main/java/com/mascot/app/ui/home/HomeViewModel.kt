package com.mascot.app.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.repository.MascotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 홈 화면 ViewModel
 * 
 * 역할:
 * - 홈 화면 상태 관리 (LOCKED, FIRST_ENTER, ROOM)
 * - 퀘스트 진행도 관리
 * - 획득한 오브제 관리
 * - 래플 팝업 상태 관리
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    /** 홈 화면 상태 */
    private val _screenState = MutableStateFlow(HomeState.LOCKED)
    val screenState: StateFlow<HomeState> = _screenState.asStateFlow()

    /** 획득한 오브제 목록 */
    private val _unlockedObjects = MutableStateFlow<List<String>>(emptyList())
    val unlockedObjects: StateFlow<List<String>> = _unlockedObjects.asStateFlow()

    /** 퀘스트 진행도 (0 ~ 3) */
    private val _questCount = MutableStateFlow(0)
    val questCount: StateFlow<Int> = _questCount.asStateFlow()

    /** 래플 팝업 표시 여부 */
    private val _showRafflePopup = MutableStateFlow(false)
    val showRafflePopup: StateFlow<Boolean> = _showRafflePopup.asStateFlow()

    /**
     * 마스코트 수집 완료 처리
     * 홈 화면을 FIRST_ENTER 상태로 변경
     */
    fun onMascotCollected() {
        _screenState.value = HomeState.FIRST_ENTER
    }

    /**
     * 첫 진입 완료 처리
     * 홈 화면을 ROOM 상태로 변경
     */
    fun finishFirstEnter() {
        // 이제 팝업을 닫고 -> 방(ROOM)으로 이동
        _screenState.value = HomeState.ROOM
    }

    /**
     * 퀘스트 진행도 증가 (디버그/테스트용)
     * 
     * 주의: 실제 프로덕션에서는 퀘스트 완료 시 호출되어야 함
     * 현재는 테스트 목적으로 수동 호출 가능
     */
    fun debugProgressQuest() {
        val currentCount = _questCount.value
        if (currentCount < 3) {
            _questCount.value = currentCount + 1

            // 퀘스트 진행도에 따라 오브제 추가
            val newObject = when (currentCount) {
                0 -> "튀김소보로"
                1 -> "한빛탑"
                2 -> "대전오월드"
                else -> ""
            }

            if (newObject.isNotEmpty() && !_unlockedObjects.value.contains(newObject)) {
                _unlockedObjects.value = _unlockedObjects.value + newObject
            }
        }
    }

    /**
     * 래플 팝업 열기
     * 퀘스트 3개 완료 시에만 표시
     */
    fun openRafflePopup() {
        if (_questCount.value >= 3) {
            _showRafflePopup.value = true
        }
    }

    /**
     * 래플 팝업 닫기
     * 팝업 닫을 때 퀘스트 진행도 초기화
     */
    fun closeRafflePopup() {
        _showRafflePopup.value = false
        _questCount.value = 0
    }
}