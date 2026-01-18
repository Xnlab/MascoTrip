package com.mascot.app.ui.encyclopedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mascot.app.data.encyclopediadata.dao.MascotDao
import com.mascot.app.data.encyclopediadata.entity.MascotEntity
import com.mascot.app.data.encyclopediadata.entity.ZoneEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 도감 화면 ViewModel
 * 
 * 역할:
 * - 마스코트 및 지역 데이터 조회
 * - 지역별 마스코트 그룹화
 */
@HiltViewModel
class EncyclopediaViewModel @Inject constructor(
    private val mascotDao: MascotDao
) : ViewModel() {

    /**
     * 모든 지역 목록 조회 (Flow)
     */
    val zones: Flow<List<ZoneEntity>> = mascotDao.observeZones()

    /**
     * 모든 마스코트 목록 조회 (Flow)
     */
    val mascots: Flow<List<MascotEntity>> = mascotDao.observeMascots()

    /**
     * 지역별 마스코트 맵 생성
     * 
     * @return zoneId를 키로 하는 마스코트 맵
     */
    fun getMascotsByZone(mascotList: List<MascotEntity>): Map<Int, MascotEntity> {
        return mascotList.associateBy { it.zoneId }
    }
}
