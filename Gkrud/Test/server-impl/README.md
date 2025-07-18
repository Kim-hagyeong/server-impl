# server-impl

Spring Boot + MyBatis + MySQL 기반 회원 & 게시판 RESTful API 서버

## 기능
1. 회원가입, 로그인/로그아웃, 내 정보 조회·수정
2. 게시글 작성·목록·상세·수정·삭제 (작성자 본인만 권한)

## 실행 방법
1. MySQL에 `server_impl` 데이터베이스 생성
2. `database.sql` 실행
3. 프로젝트 루트에서 환경변수 설정
   ```bash
   export DB_USER=root
   export DB_PASS=1111
