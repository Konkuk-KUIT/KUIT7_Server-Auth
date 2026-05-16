
# 8주차 실습: JWT 기반 인증/인가 시스템 구현

## 목표
- Spring Boot와 JWT를 사용하여 인증/인가 시스템을 구현합니다.
- Spring Interceptor를 활용하여 API 보호.
- Postman을 사용하여 API 요청을 테스트합니다.

## 요구 사항
1. `/login` API를 통해 JWT 토큰을 발급받습니다.
2. `/profile` API는 인증된 사용자만 접근할 수 있습니다.
3. `/admin` API는 관리자 권한이 있는 사용자만 접근할 수 있습니다.

## 구현 과제
1. **UserController.java**
   - `/profile` API: JWT 토큰을 검증하여 로그인된 사용자만 접근 가능하도록 구현합니다.
   - `/admin` API: 관리자 권한 (`ROLE_ADMIN`)이 있는 사용자만 접근 가능하도록 구현합니다.
2. (선택) AccessToken 만료 시 RefreshToken 발급
3. (선택) ArgumentResolver 이용 개선

## Postman 테스트 예시
1. **로그인 요청**
   POST /login { "username": "admin", "password": "admin123" }

2. **프로필 조회**
   GET /profile Authorization: Bearer <JWT_TOKEN>

3. **관리자 페이지 조회**
   GET /admin Authorization: Bearer <JWT_TOKEN>

