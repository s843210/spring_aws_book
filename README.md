# 스프링 부트 AWS 마이그레이션

## 프로젝트 소개
이 프로젝트는 '스프링 부트와 AWS로 혼자 구현하는 웹 서비스' 도서의 예제를 바탕으로 하며, Spring Boot 2.x **Spring Boot 3.x**로 마이그레이션을 했습니다. 

## 주요 변경 사항 (Spring Boot 2.x -> 3.x 마이그레이션)
원본 도서의 코드는 Spring Boot 2.x를 기준으로 작성되었으나, 현재 버전에 맞게 다음과 같은 마이그레이션 작업을 수행했습니다.


    
1.  **Jakarta EE 적용**
    - Java EE가 Eclipse Foundation으로 이관됨에 따라 패키지명이 변경되었습니다.
    - `javax.persistence.*` -> `jakarta.persistence.*` (JPA 엔티티 등)
    - `javax.servlet.*` -> `jakarta.servlet.*`

2.  **Spring Security 6.x 적용**
    - 기존 `WebSecurityConfigurerAdapter` 상속 방식이 Deprecated 되었습니다.
    - **`SecurityFilterChain`을 Bean으로 등록**하는 최신 방식으로 보안 설정을 전면 수정했습니다.
    - `antMatchers()` -> `requestMatchers()`로 메서드명이 변경되었습니다.

3.  **Gradle 설정 업데이트**
    - Spring Boot 3.x 버전에 맞춰 플러그인 및 의존성 버전을 최신화했습니다.

## 시작하기 (Getting Started)

### 사전 요구 사항 (Prerequisites)
- **Java 17** 이상
- Gradle

### 설정 (Configuration)
이 프로젝트는 인증을 위해 OAuth2를 사용합니다. 보안상의 이유로 민감한 키 값이 포함된 `application-oauth.properties` 파일은 저장소에 포함되어 있지 않습니다.

로컬에서 프로젝트를 실행하려면 제공된 샘플 파일을 기반으로 해당 파일을 직접 생성해야 합니다.

1. 샘플 파일을 복사합니다:
   ```bash
   cp src/main/resources/application-oauth.properties.sample src/main/resources/application-oauth.properties
   ```

2. `src/main/resources/application-oauth.properties` 파일을 열고 아래 항목들을 실제 OAuth 자격 증명(Credential)으로 교체하세요:
   - `YOUR_GOOGLE_CLIENT_ID`
   - `YOUR_GOOGLE_CLIENT_SECRET`
   - `YOUR_NAVER_CLIENT_ID`
   - `YOUR_NAVER_CLIENT_SECRET`

### 빌드 및 실행 (Build and Run)
```bash
./gradlew build
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```
