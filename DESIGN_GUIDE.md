# 포켓캠프 스타일 디자인 가이드

## 🎨 디자인 컨셉

동물의숲 포켓캠프에서 영감을 받은 **밝고 따뜻한 캐주얼 스타일**

### 핵심 키워드
- 🌸 밝고 따뜻한 색감 (크림색, 파스텔 톤)
- 🔵 부드러운 둥근 모서리
- ✨ 친근하고 편안한 느낌
- 🎴 카드형 레이아웃
- 🌈 지역 마스코트에 부합하는 컬러

---

## 🎨 색상 팔레트

### Primary Colors (주요 색상)
```kotlin
MascotPrimary = Color(0xFFFFB6C1)      // 연한 핑크
MascotSecondary = Color(0xFF87CEEB)    // 하늘색
```

### Background Colors (배경)
```kotlin
MascotBackground = Color(0xFFFFF8DC)   // 크림색 배경
MascotSurface = Color(0xFFFFFEF5)      // 거의 흰색 표면
CardBackground = Color(0xFFFFFFFF)     // 흰색 카드
```

### Accent Colors (강조)
```kotlin
MascotAccent = Color(0xFFFFD700)       // 골드 (완료, 보상)
MascotSuccess = Color(0xFF90EE90)      // 연한 초록
MascotError = Color(0xFFFF6B6B)        // 연한 빨강
```

### 지역별 색상
```kotlin
RegionJunggu = Color(0xFFFFB6C1)      // 중구 - 핑크
RegionSeogu = Color(0xFF87CEEB)       // 서구 - 하늘색
RegionYuseong = Color(0xFFDDA0DD)      // 유성구 - 자주색
RegionDaedeok = Color(0xFF98D8C8)      // 대덕구 - 민트
RegionDonggu = Color(0xFFFFD700)       // 동구 - 골드
```

---

## 📐 Shape (모서리)

모든 요소에 **둥근 모서리** 적용:

```kotlin
extraSmall = 8.dp   // 작은 요소 (아이콘, 칩)
small = 12.dp       // 작은 카드, 버튼
medium = 16.dp      // 일반 카드, 다이얼로그
large = 24.dp       // 큰 카드, 모달
extraLarge = 32.dp  // 특별한 요소
```

---

## 📝 타이포그래피

친근하고 읽기 쉬운 폰트:
- **제목**: Bold, 18-32sp
- **본문**: Normal, 14-16sp
- **라벨**: Medium, 12-14sp

---

## 🎴 카드 스타일

### 기본 카드
- 배경: 흰색 (`CardBackground`)
- 모서리: `medium` (16.dp)
- 그림자: 4.dp (부드러운 그림자)
- 패딩: 18.dp

### 완료 카드
- 배경: 연한 초록 (`MascotSuccess.copy(alpha = 0.15f)`)
- 체크마크 아이콘 포함

---

## 🖼️ 화면별 적용

### 1. 홈 화면
- ✅ 크림색 배경 (`MascotBackground`)
- ✅ 포켓캠프 스타일 진행도 카드
- ✅ 둥근 모서리 버튼

### 2. 퀘스트 화면
- ✅ 크림색 배경
- ✅ 포켓캠프 스타일 지역 필터 칩
- ✅ 부드러운 카드 디자인
- ✅ 포켓캠프 스타일 FAB

### 3. 도감 화면
- ✅ 크림색 배경 (기존 어두운 배경 제거)
- ✅ 포켓캠프 스타일 마스코트 카드
- ✅ 지역별 색상 헤더

---

## 💡 사용 가이드

### 새로운 컴포넌트 만들 때

```kotlin
// ✅ 포켓캠프 스타일 카드
Surface(
    shape = MaterialTheme.shapes.medium,  // 둥근 모서리
    color = CardBackground,               // 흰색 배경
    shadowElevation = 4.dp,              // 부드러운 그림자
    modifier = Modifier.padding(8.dp)
) {
    // 내용
}

// ✅ 포켓캠프 스타일 버튼
Surface(
    onClick = { /* ... */ },
    shape = MaterialTheme.shapes.medium,
    color = MascotPrimary,               // 연한 핑크
    modifier = Modifier.padding(8.dp)
) {
    Text(
        text = "버튼",
        style = MaterialTheme.typography.labelLarge,
        color = MascotOnPrimary
    )
}
```

### 색상 사용
- **배경**: 항상 `MascotBackground` 사용
- **카드**: `CardBackground` (흰색)
- **버튼**: `MascotPrimary` (연한 핑크) 또는 `MascotSecondary` (하늘색)
- **텍스트**: `MascotOnBackground` 또는 `MascotOnSurface`

---

## 🎯 다음 단계 (추가 개선 가능)

1. **그라데이션 배경**: 홈 화면에 부드러운 그라데이션 추가
2. **애니메이션**: 카드 등장 시 부드러운 애니메이션
3. **일러스트 스타일**: 마스코트 이미지를 더 포켓캠프 느낌으로
4. **아이콘**: 더 친근한 스타일의 아이콘 사용
5. **폰트**: 커스텀 폰트 추가 (선택사항)

---

## 📚 참고

- 동물의숲 포켓캠프 UI/UX 참고
- 밝고 따뜻한 색감 유지
- 모든 요소에 둥근 모서리 적용
- 부드러운 그림자 사용
