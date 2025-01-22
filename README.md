# Spring Cloud User Service

## 프로젝트 소개
Spring Cloud 기반의 마이크로서비스 아키텍처를 사용한 사용자 서비스입니다. 이 서비스는 사용자 관리, 인증, 그리고 주문 서비스와의 상호작용을 담당합니다.

## 기술 스택
- Java 17
- Spring Boot 3.2.4
- Spring Cloud 2023.0.1
- Spring Security
- Spring Data JPA
- H2 Database
- Eureka Client
- OpenFeign
- RabbitMQ
- Gradle

## 프로젝트 구조
```
src/main/java/com/example/userservice/
├── client/         # 서비스 간 통신 인터페이스
├── common/
│   └── security/   # 보안 설정 및 인증 필터
├── controller/     # REST API 엔드포인트
├── dto/           # 데이터 전송 객체
├── error/         # 에러 처리
├── jpa/           # 엔티티 및 레포지토리
├── service/       # 비즈니스 로직
└── vo/            # 요청/응답 값 객체
```

## 주요 기능
1. 사용자 관리
   - 사용자 생성
   - 사용자 조회
   - 전체 사용자 목록 조회

2. 보안
   - JWT 기반 인증
   - BCrypt 패스워드 암호화
   - IP 기반 접근 제어

3. 서비스 연동
   - Eureka 서비스 디스커버리
   - Feign Client를 통한 주문 서비스 연동

## API 엔드포인트
- `GET /health_check`: 서비스 상태 확인
- `GET /welcome`: 환영 메시지
- `POST /users`: 새 사용자 생성
- `GET /users`: 전체 사용자 조회
- `GET /users/{userId}`: 특정 사용자 조회

## 설정
### 애플리케이션 설정
- 서버 포트: 랜덤 할당 (0)
- H2 콘솔: 활성화 (/h2-console)
- Eureka 클라이언트: 활성화

### 보안 설정
- CSRF 보호: 비활성화
- 프레임 옵션: SAMEORIGIN
- 세션 관리: STATELESS
- IP 기반 접근 제어: 127.0.0.1, 172.30.1.48

## 빌드 및 실행
### 빌드
```bash
./gradlew build
```

### 테스트
```bash
./gradlew test
```

## 모니터링
Actuator 엔드포인트를 통해 다음 기능이 제공됩니다:
- refresh
- health
- beans
- busrefresh

## 환경 요구사항
- JDK 17
- Gradle 7.x 이상
- RabbitMQ 서버
- Spring Cloud Config 서버 (8888 포트)
