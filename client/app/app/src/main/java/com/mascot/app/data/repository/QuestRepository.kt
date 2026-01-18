package com.mascot.app.data.repository

import com.mascot.app.data.model.QuestItem
import com.mascot.app.data.remote.QuestApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 퀘스트 데이터 Repository
 * 
 * 역할:
 * - 서버에서 퀘스트 데이터 조회
 * - 지역별 퀘스트 목록 관리
 * - 에러 처리 및 빈 리스트 반환
 */
@Singleton
class QuestRepository @Inject constructor(
    private val api: QuestApi
) {

    /**
     * 전체 구에 대한 퀘스트 목록 조회
     * 
     * @param userId 사용자 ID
     * @return 지역별 퀘스트 맵 (지역명 -> 퀘스트 목록)
     * 
     * 주의: 특정 지역 조회 실패 시 빈 리스트로 처리하여 다른 지역 데이터는 정상 반환
     */
    suspend fun getAllQuests(userId: String): Map<String, List<QuestItem>> {
        val regions = listOf("중구", "서구", "유성구", "대덕구", "동구")
        val result = mutableMapOf<String, List<QuestItem>>()

        for (region in regions) {
            try {
                val response = api.getQuestsByRegion(region, userId)
                result[region] = response.quests
            } catch (e: Exception) {
                // 특정 지역 조회 실패 시 빈 리스트로 처리
                // 다른 지역 데이터는 정상적으로 반환됨
                result[region] = emptyList()
            }
        }
        return result
    }
}
