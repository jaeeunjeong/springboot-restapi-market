# SpringBoot-restapi-market
## 목차
- 요구 사항
- 기술 스택
- 프로젝트 중점 사항
## Spring Boot를 이용해서 상품 거래 api 구축
- [x] 회원 관리
  - [x] 토큰 생성
  - [x] 회원 가입
  - [x] 로그인
  - [x] 예외 처리
  - [x] 회원 정보 조회
- [x] 카테고리
- [ ] 게시판
    - [ ] 조회, 생성, 수정, 삭제
    - [ ] 댓글
- [ ] 사용자간 메시지
- [ ] 페이징 처리
- [ ] 로그 기능
## 기술 스택
Java 8, SpringBoot, H2, JPA, Gradle
## 프로젝트 중점 사항(wiki 참고)
- 로그인에서 반복되는 서비스를 AOP
- 로그인 서비스를 추상화
- Swagger를 이용한 API 문서화
- 카테고리의 계층화 

## 참고
https://github.com/SongHeeJae/kuke-market
