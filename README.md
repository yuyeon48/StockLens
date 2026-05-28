# 📈 StockLens

> 한국 시가총액 Top50 실시간 주가 조회 및 AI 종목 추천 서비스

## 🔗 주요 기능

- **실시간 주가 조회** : 네이버 금융 크롤링으로 시가총액 Top50 수집 후 한국투자증권 Open API로 실시간 주가 조회
- **주가 차트** : 종목 클릭 시 일봉/주봉 차트 팝업
- **AI 종목 추천** : Ollama(gemma3:1b) 기반 로컬 AI가 현재 데이터 분석 후 투자 유망 종목 추천
- **관심종목** : 로그인 유저의 관심종목 저장 및 별도 탭 조회
- **회원 인증** : 일반 회원가입/로그인 + Google OAuth2 소셜 로그인

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Spring Boot 3.5, Spring Security, Spring Data JPA |
| Database | MySQL 8.0 |
| Frontend | Thymeleaf, Chart.js |
| AI | Ollama (gemma3:1b) |
| API | 한국투자증권 Open API, 네이버 금융 크롤링 (Jsoup) |
| 문서화 | Swagger (SpringDoc OpenAPI 3) |
| 인증 | Spring Security, OAuth2 (Google) |

## 📁 프로젝트 구조

```
src/main/java/com/example/Stock
├── controller
│   ├── StockController.java
│   └── AuthController.java
├── service
│   ├── StockService.java
│   ├── KisTokenService.java
│   ├── AiRecommendService.java
│   ├── WatchListService.java
│   ├── UserService.java
│   ├── CustomOAuth2UserService.java
│   └── CustomUserDetailsService.java
├── domain
│   ├── Stock.java
│   ├── User.java
│   └── WatchList.java
└── repository
    ├── StockRepository.java
    ├── UserRepository.java
    └── WatchListRepository.java
```

## ⚙️ 실행 방법

### 1. 사전 준비
- Java 17
- MySQL 8.0
- Ollama 설치 및 gemma3:1b 모델 다운로드

```bash
ollama pull gemma3:1b
```

### 2. DB 설정

```sql
CREATE DATABASE stocklens;
CREATE USER 'stockuser'@'localhost' IDENTIFIED BY 'stockpass123';
GRANT ALL PRIVILEGES ON stocklens.* TO 'stockuser'@'localhost';
```

### 3. application.properties 설정

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stocklens?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
spring.datasource.username=stockuser
spring.datasource.password=stockpass123

kis.api.appkey=YOUR_APP_KEY
kis.api.appsecret=YOUR_APP_SECRET
kis.api.base-url=https://openapi.koreainvestment.com:9443

spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile
```

### 4. 실행

```bash
./gradlew bootRun
```

접속: http://localhost:8080/stocks

## 📌 API 문서
http://localhost:8080/swagger-ui/index.html
## 📸 스크린샷

> 스크린샷 추가 예정
<img width="671" height="502" alt="스크린샷 2026-05-28 오후 3 56 16" src="https://github.com/user-attachments/assets/e6e7754a-de59-426d-ac6d-968f80ac4d3f" />
<img width="870" height="548" alt="스크린샷 2026-05-28 오후 3 56 09" src="https://github.com/user-attachments/assets/e3375a72-6d5e-472a-944d-7929d820f35b" />

## ⚠️ 주의사항

- 한국투자증권 Open API는 실계좌 기준으로 동작합니다
- AI 추천은 투자 참고용이며 실제 투자 결정은 본인이 해야 합니다
- API 키는 절대 GitHub에 올리지 마세요 (`.gitignore`에 `application.properties` 추가)

