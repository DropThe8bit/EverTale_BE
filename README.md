# EverTale_BE
🏅 캡스톤 디자인 경진 대회 은상 수상 (2025.12) 🏅 <br>
🏅 졸업 프로젝트 포스터 세션 우수상 수상 (2025.12) 🏅 <br>

## 🛠️ Backend 기술 스택

| 구성 요소 | 사용 기술 |
| --- | --- |
| **프레임워크** | Spring Boot, Spring Security, Spring Web, Spring Data JPA |
| **인증** | OAuth 2.0 |
| **서버 / 배포** | AWS EC2, Docker |
| **파일 저장소** | AWS S3 |

## 🗄️ 데이터베이스

| 구성 요소 | 사용 기술 |
| --- | --- |
| **RDBMS** | MySQL |
| **캐시 / 세션 저장소** | Redis |

<br>

## 🛠️ 프로젝트 Convention

### ✅ Package

#### 디렉토리 구조 전략

##### `domain`
- `controller`
- `dto`  
  - 정적 팩토리 메소드로 entity ↔ dto
- `entity`  
  - `enum`
- `repository`
- `service`

##### `global`
- `config` : security, aws 등 설정 정보
- `entity` : 공통 엔티티 (예: `BaseTimeEntity`)
- `payload` : 응답 관련 구조  
  - `code`, `exception`
- `validation` : 커스텀 유효성 검증

## 로컬 실행 방법
### 1. git clone
```
git clone https://github.com/DropThe8bit/EverTale_BE.git
cd evertale_be
```
### 2. Environment Variables 생성
- application.yml 파일을 생성하고 다음 환경변수들을 입력합니다.
```
DB_URL=
DB_USER=
DB_PASSWORD=

SPRING_SERVER_SERVLET_CONTEXT_PATH=

AWS_ACCESS_KEY=
AWS_SECRET_ACCESS_KEY=
AWS_S3_BUCKET=

CLIENT_ID=
CLIENT_SECRET=
REDIRECT_URI=
SECRET_KEY=

AI_BASE_URL=
```
#### application.yml
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
        use_sql_comments: true
        jdbc:
          time_zone: Asia/Seoul
  data:
    redis:
      host: localhost
      port: 6379

  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 20MB

server:
   servlet:
     context-path: ${SPRING_SERVER_SERVLET_CONTEXT_PATH}

springdoc:
  swagger-ui:
    path: /swagger
    groups-order: DESC
    tags-sorter: alpha
    operations-sorter: method

cloud:
  aws:
    s3:
      bucket: ${AWS_S3_BUCKET}
    region:
      static: ap-northeast-2
    credentials:
      accessKey: ${AWS_ACCESS_KEY}
      secretKey: ${AWS_SECRET_ACCESS_KEY}

jwt:
  secret-key: ${SECRET_KEY}

naver:
  client-id: ${CLIENT_ID}
  client-secret: ${CLIENT_SECRET}
  redirect-uri: ${REDIRECT_URI}

ai:
  base-url: ${AI_BASE_URL}
```

### 3. 로컬 실행
- 프로젝트 상단 `Run 'EverTale'`을 클릭하여 어플리케이션을 실행합니다.
