# 사업자 등록번호 검증 시스템

이 프로젝트는 **사업자 등록번호와 사업자 등록증 이미지를 업로드**하면
AI와 OCR을 활용해 사업자 등록증의 진위 여부를 판별해주는 웹 서비스입니다.

---

## 주요 기능

- 사업자 등록번호 입력 및 사업자 등록증 이미지 업로드
- OCR을 통한 이미지 내 사업자번호 추출
- AI 기반 사업자 등록증 진위 판별
- 결과(입력값, OCR값, AI 판독 결과) 시각화

---

## 프로젝트 구조

```
├── frontend/           # 프론트엔드(Vue3, Vite)
│   └── bizNumberCheck/
│       ├── src/
│       │   ├── views/         # 주요 페이지(HomeView.vue)
│       │   ├── components/    # (필요시) 컴포넌트
│       │   ├── router/        # 라우터 설정
│       │   └── ...            
│       ├── public/            # 정적 파일
│       ├── package.json       # 프론트엔드 의존성
│       └── ...
├── api/                # 백엔드(Spring Boot)
│   ├── src/
│   │   └── main/java/com/bizcheck/system/
│   │       ├── controller/    # bizCheckController.java (API 엔드포인트)
│   │       ├── service/       # BizCheckService.java (비즈니스 로직)
│   │       ├── RequestDTO.java
│   │       └── ResponseDTO.java
│   ├── build.gradle           # 백엔드 의존성
│   └── ...
└── readme.md           # 프로젝트 설명 파일
```

---

## 프론트엔드

- **프레임워크:** Vue 3 + Vite
- **주요 라이브러리:** axios, vue-router
- **개발/빌드 명령어:**
  ```bash
  npm install
  npm run dev      # 개발 서버 실행
  npm run build    # 프로덕션 빌드
  ```

### 주요 화면

- 사업자 등록번호 입력
- 사업자 등록증 이미지 업로드(이미지 파일만 가능, 5MB 이하)
- 검증 결과(입력값, OCR값, AI 판독 결과 및 상세 메시지) 표시

---

## 백엔드

- **프레임워크:** Spring Boot 3
- **주요 라이브러리:** spring-boot-starter-web, lombok, okhttp, spring-ai
- **JDK:** 17

### 주요 API

- `POST /api/bizCheck`
  - **요청:**  
    - `businessNumber` (String)  
    - `file` (MultipartFile, 사업자 등록증 이미지)
  - **응답:**  
    ```json
    {
      "businessNumber": "입력한 사업자번호",
      "ocrNumber": "이미지에서 추출된 사업자번호",
      "isValid": true,
      "message": "AI 판독 상세 메시지"
    }
    ```

---

## 개발 환경 및 기타

- **IDE:** VSCode,IntelliJ IDEA
- **환경 변수:**  
  - `.env` 파일 등 민감 정보는 `.gitignore`에 추가하여 관리

---

## 기타

- 문의/이슈는 깃허브 이슈 또는 메일로 남겨주세요.
