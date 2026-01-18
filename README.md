# MascoTrip

대전 지역 기반 AR 여행 퀘스트 게임 앱

## 📱 프로젝트 개요

MascoTrip은 위치 기반 AR 기술을 활용하여 대전 지역의 마스코트를 수집하고, 맞춤형 퀘스트를 수행하는 모바일 앱입니다.

## 🏗️ 프로젝트 구조

```
korail-hackathon/
├── client/          # Android 앱 (Kotlin + Jetpack Compose)
│   └── app/
│       └── src/
│           ├── main/java/    # 소스 코드
│           ├── main/res/      # 리소스
│           └── main/assets/  # 에셋 파일
│
└── server/         # Node.js 백엔드
    ├── server.js    # Express 서버
    ├── models/      # MongoDB 모델
    └── package.json # 의존성
```

## 🚀 시작하기

### 사전 요구사항
- Android Studio (최신 버전)
- Node.js 18+
- MongoDB
- OpenAI API Key

### 클라이언트 실행
```bash
cd client/app
./gradlew assembleDebug
```

### 서버 실행
```bash
cd server
npm install
npm start
```

## 👥 협업 가이드

자세한 협업 가이드는 [CONTRIBUTING.md](./CONTRIBUTING.md)를 참고하세요.

### 빠른 시작
1. `develop` 브랜치에서 작업 브랜치 생성
2. 작업 완료 후 Pull Request 생성
3. 코드 리뷰 후 머지

## 📝 주요 기능

- 🏠 **홈 화면**: 마스코트 방 및 퀘스트 진행도
- 🎯 **퀘스트**: 지역별 맞춤형 퀘스트 수행
- 📷 **AR 모드**: ARCore를 활용한 마스코트 수집
- 📚 **도감**: 수집한 마스코트 확인

## 🛠️ 기술 스택

### 클라이언트
- Kotlin
- Jetpack Compose
- Hilt (의존성 주입)
- Room (로컬 DB)
- Retrofit (네트워크)
- ARCore
- ML Kit OCR

### 서버
- Node.js
- Express
- MongoDB
- OpenAI GPT-4o

## 📄 라이선스

이 프로젝트는 창업 프로젝트입니다.
