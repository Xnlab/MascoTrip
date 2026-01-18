# 협업 가이드 (Contributing Guide)

## 📋 브랜치 전략

### 메인 브랜치
- `main`: 프로덕션 배포용 (항상 안정적이어야 함)
- `develop`: 개발 통합 브랜치 (기능 개발 완료 후 먼저 여기로)

### 작업 브랜치 네이밍 규칙
```
feature/기능명-작업자명
예: feature/3d-village-ui-Xnlab
예: feature/unity-ar-integration-Jung1

bugfix/버그명-작업자명
예: bugfix/quest-completion-error-Xnlab

hotfix/긴급수정명-작업자명
예: hotfix/crash-on-startup-Xnlab
```

## 🔄 작업 흐름

### 1. 새 기능 개발 시작
```bash
# develop 브랜치에서 최신 코드 가져오기
git checkout develop
git pull origin develop

# 새 기능 브랜치 생성
git checkout -b feature/기능명-작업자명

# 작업 후 커밋
git add .
git commit -m "feat: 기능 설명"
```

### 2. 작업 완료 후 PR 생성
1. GitHub에서 Pull Request 생성
2. `develop` 브랜치로 PR 요청
3. 리뷰어 지정 (최소 1명)
4. 리뷰 통과 후 머지

### 3. 커밋 메시지 규칙
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅 (기능 변경 없음)
refactor: 코드 리팩토링
test: 테스트 추가/수정
chore: 빌드 설정, 패키지 관리 등

예시:
feat: 3D 마을 화면 UI 구현
fix: 퀘스트 완료 시 크래시 수정
refactor: ViewModel 의존성 주입 개선
```

## 👥 역할 분담 예시

### 프론트엔드 (Android)
- **UI 개발자 1**: 홈 화면, 3D 마을 구현
- **UI 개발자 2**: 퀘스트 화면, 도감 화면
- **AR 개발자**: Unity AR 통합, AR 기능 개발
- **공통**: 네비게이션, 공통 컴포넌트

### 백엔드 (Node.js)
- **API 개발자**: 서버 API 개발, 데이터베이스 설계
- **AI 통합**: GPT API 연동, 퀘스트 생성 로직

## ✅ PR 체크리스트

PR 생성 전 확인사항:
- [ ] 코드가 정상적으로 빌드됨
- [ ] 린터 오류 없음
- [ ] 관련 이슈 번호 포함
- [ ] 변경사항 설명 작성
- [ ] 리뷰어 지정

## 🚫 주의사항

1. **절대 `main` 브랜치에 직접 푸시하지 않기**
2. **다른 사람의 브랜치에 직접 푸시하지 않기**
3. **충돌 발생 시 팀원과 상의 후 해결**
4. **작업 전 항상 최신 코드 pull 받기**
