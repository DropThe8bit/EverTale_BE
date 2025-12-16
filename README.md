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
