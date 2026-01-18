package com.mascot.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices

/**
 * 위치 정보 유틸리티
 * 
 * 역할:
 * - 현재 위치 조회 (Fused Location Provider 사용)
 * - 위치 권한은 호출 전에 확인되어야 함
 */
object LocationHelper {

    private const val TAG = "LocationHelper"

    /**
     * 현재 위치 조회
     * 
     * @param context 컨텍스트
     * @param onResult 위치 조회 성공 시 호출되는 콜백 (위도, 경도)
     * 
     * 주의: 위치 권한이 허용되어 있어야 함
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (lat: Double, lng: Double) -> Unit
    ) {
        Log.d(TAG, "📍 getCurrentLocation() 호출됨")

        val fusedClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(
                        TAG,
                        "✅ 위치 수신 성공 → lat=${location.latitude}, lng=${location.longitude}"
                    )
                    onResult(location.latitude, location.longitude)
                } else {
                    Log.e(TAG, "❌ location == null (GPS 꺼짐 / 위치 기록 없음)")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "❌ 위치 요청 실패", e)
            }
    }
}
