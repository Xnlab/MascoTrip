package com.mascot.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 포켓캠프 스타일 Shape 정의
 * 
 * 동물의숲 포켓캠프에서 영감을 받은 둥근 모서리
 * - 모든 요소에 부드러운 둥근 모서리 적용
 * - 친근하고 캐주얼한 느낌
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // 작은 요소 (아이콘, 칩 등)
    small = RoundedCornerShape(12.dp),       // 작은 카드, 버튼
    medium = RoundedCornerShape(16.dp),      // 일반 카드, 다이얼로그
    large = RoundedCornerShape(24.dp),       // 큰 카드, 모달
    extraLarge = RoundedCornerShape(32.dp)    // 특별한 요소 (홈 화면 카드 등)
)
