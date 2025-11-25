# Spring Boot AWS 배포 일지

## 1. 배포 환경
- **서버**: AWS EC2 (Amazon Linux 2023)
- **데이터베이스**: AWS RDS (MySQL 8.0)
- **Java 버전**: JDK 21 (Temurin)
- **빌드 도구**: Gradle

## 2. 주요 작업 내용

### 2.1 EC2 서버 설정
- 인스턴스 생성 및 보안 그룹 설정 (SSH 22, HTTP 8080 오픈)
- 타임존 변경 (Asia/Seoul)
- Java 21 설치 (`java-21-amazon-corretto`)
- Git 설치 및 프로젝트 클론 (또는 수동 업로드)

### 2.2 RDS 데이터베이스 설정
- MySQL 8.0 인스턴스 생성
- 파라미터 그룹 설정 (`utf8mb4` - 한글 깨짐 방지)
- 보안 그룹 설정 (EC2 및 로컬 IP 접속 허용)
- 초기 데이터베이스 생성 (`spring_aws_book`)

### 2.3 애플리케이션 설정
- `build.gradle`: MySQL 커넥터 의존성 추가
- `application-real.properties`: 배포용 프로필 설정 (Git 제외)
- `application-real-db.properties`: DB 접속 정보 설정 (Git 제외, 서버 직접 생성)
- `application-oauth.properties`: OAuth 설정 (Git 제외, 서버 직접 생성)

### 2.4 배포 트러블 슈팅 (Trouble Shooting)
1. **SSH 및 DB 접속 타임아웃 (Network/Security)**
   - **문제**: 로컬 터미널에서 EC2 접속 시도 및 EC2에서 RDS 접속 시도 시 `Operation timed out` 발생.
   - **원인**: AWS 보안 그룹(Security Group)의 인바운드 규칙 미설정.
   - **해결**: 
     - SSH(22): 내 IP(My IP)만 허용하도록 규칙 추가.
     - MySQL(3306): EC2의 보안 그룹 ID를 소스로 지정하여, **오직 웹 서버(EC2)에서만 DB에 접근 가능하도록** 보안 강화.

2. **권한 관리 취약점 해결 (Security/IDOR)**
   - **문제**: API 호출 시 게시글 ID만 있으면 작성자가 아니어도 수정/삭제가 가능한 논리적 결함 발견.
   - **해결**: `PostsService`에 검증 로직 추가. 게시글의 작성자 ID(`user.id`)와 현재 로그인한 세션 유저의 ID를 비교하여, 일치하지 않을 경우 `403 Forbidden` 예외 발생시킴.

3. **OAuth 리디렉션 불일치 (External API)**
   - **문제**: 배포 후 소셜 로그인 시 `redirect_uri_mismatch` 에러 발생.
   - **원인**: OAuth Provider(Google/Naver)에 등록된 리디렉션 URI가 `localhost`로만 설정됨.
   - **해결**: EC2 퍼블릭 IP 및 도메인 주소를 개발자 센터의 '승인된 리디렉션 URI' 목록에 추가 등록.

4. **포트 충돌 및 프로세스 관리 (Linux/Operation)**
   - **문제**: 재배포 시 "Web server failed to start. Port 8080 was already in use." 에러 발생.
   - **해결**: `ps -ef | grep java`로 실행 중인 프로세스(PID)를 확인하고 `kill -9 [PID]`로 종료. 이후 `nohup`을 사용하여 터미널 세션이 끊겨도 애플리케이션이 유지되도록 백그라운드 실행 환경 구축.

## 3. 최종 실행 명령어
```bash
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties,/home/ec2-user/app/application-real.properties \
    -Dspring.profiles.active=prod,oauth,real-db \
    /home/ec2-user/*.jar > /home/ec2-user/nohup.out 2>&1 &
```
