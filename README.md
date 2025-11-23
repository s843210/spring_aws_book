# 스프링 부트 AWS 도서 프로젝트

## 시작하기 (Getting Started)

### 사전 요구 사항 (Prerequisites)
- Java 17 이상
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
