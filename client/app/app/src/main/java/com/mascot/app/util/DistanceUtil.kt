package com.mascot.app.util

import android.location.Location

/**
 * 두 지점 간 거리 계산 (미터 단위)
 * 
 * @param lat1 첫 번째 지점의 위도
 * @param lng1 첫 번째 지점의 경도
 * @param lat2 두 번째 지점의 위도
 * @param lng2 두 번째 지점의 경도
 * @return 두 지점 간 거리 (미터)
 * 
 * 사용 예: 퀘스트 위치와 현재 위치 간 거리 계산
 */
fun getDistanceMeter(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double
): Float {
    val result = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, result)
    return result[0]
}
