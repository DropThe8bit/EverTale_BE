# EverTale_BE

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
