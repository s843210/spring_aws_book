# 🚀 Spring Boot AWS 웹 서비스

> **스프링 부트와 AWS로 혼자 구현하는 웹 서비스**
> 
> 책의 예제를 기반으로 **Spring Boot 3.x & Java 21** 최신 환경으로 리팩토링하고, **AWS 클라우드 인프라**를 직접 구축하여 배포한 프로젝트입니다.

<br>

## 🌐 배포 사이트
👉 **Live Demo**: *[배포 링크는 포트폴리오에 별도 첨부]*
> *Google / Naver 소셜 로그인을 통해 글쓰기 기능을 체험하실 수 있습니다.*

<br>

## 🛠 기술 스택 (Tech Stack)

### Backend
- **Java 21**
- **Spring Boot 3.2**
- **Spring Data JPA**
- **Spring Security & OAuth 2.0**
- **Gradle**

### Infrastructure & Database
- **AWS EC2** (Amazon Linux 2023)
- **AWS RDS** (MySQL 8.0)
- **H2 Database** (Local/Test)

<br>

## ✨ 주요 기능 (Key Features)

1.  **게시글 CRUD**
    - 게시글 등록, 수정, 조회, 삭제 API 구현
    - JPA Auditing을 이용한 생성/수정 시간 자동화 (`BaseTimeEntity`)

2.  **OAuth 2.0 소셜 로그인**
    - **Google**, **Naver** 계정을 연동한 간편 로그인/회원가입
    - `SessionUser`를 활용한 세션 기반 인증
    - ⚠️ **Naver 로그인 주의사항**: 실제 서비스가 아닌 경우(테스트, 데모, 과제, 포트폴리오 등) 네이버 검수가 반려되므로, **'개발중'** 모드에서 **테스터 아이디를 별도로 등록**하여 사용해야 합니다.

3.  **권한 관리 (Security)**
    - 로그인한 사용자만 게시글 작성/수정 가능
    - **서버 사이드 검증**: 타인의 게시글을 수정/삭제하지 못하도록 ID 기반의 권한 체크 로직 구현 (IDOR 방지)

<br>

## ☁️ 인프라 아키텍처 (Infrastructure)

- **EC2 (Web Server)**: Amazon Linux 2023 환경 구축, 타임존(Asia/Seoul) 설정
- **RDS (Database)**: MySQL 8.0, UTF-8(utf8mb4) 인코딩 설정
- **Security Group**: EC2와 RDS 간의 안전한 연결을 위한 인바운드 규칙 설계
- **Deployment**: `nohup`을 활용한 백그라운드 무중단 실행 환경 구성

<br>

## 📝 트러블 슈팅 (Trouble Shooting)

배포 과정에서 발생한 주요 이슈와 해결 과정을 기록했습니다.
👉 **[상세 배포 일지 보러가기 (DEPLOY_LOG.md)](DEPLOY_LOG.md)**

- **환경 불일치 해결**: 로컬(Mac/JDK21)과 서버(Linux/JDK17)의 버전 차이로 인한 실행 오류 해결 (Environment Parity)
- **네트워크 보안**: AWS Security Group 설정을 통한 SSH/DB 접속 타임아웃 해결
- **OAuth 리디렉션**: 배포 IP/도메인 변경에 따른 OAuth Provider(Google/Naver) 리디렉션 URI 불일치 문제 해결
