<h1 align="center">MoneySwagger API</h1>
<br>
<br>


## 🎈 기술 스택
- Java 11
- Spring boot v2.4.3
- H2 Database v1.4.199
- Spring Data JPA
- JUnit, Mockito
<br>

## 🎈어플리케이션 빌드 및 실행 방법
1) h2 DB 실행
2) 프로젝트 디렉토리로 이동
3) 터미널에 아래 명령어 실행
```
./gradlew build && java -jar ./build/libs/moneyswagger-0.0.1-SNAPSHOT.jar
```
<br>

## 🎈API 문서  (Spring Rest Docs 이용한 API 문서 자동화) 
1. h2 DB 실행
2. 어플리케이션 실행 ('어플리케이션 빌드 및 실행 방법' 참고) 
3. 웹 브라우저 실행 후 아래 URL 접속 또는 [링크](http://localhost:8080/docs/api-guide.html) 클릭 
```
http://localhost:8080/docs/api-guide.html
```
<br>

## 🎈API 안내
#### moneyswagging 모듈 : 기능 요구사항인 머니뿌리기 API 생성 용도 
- **머니뿌리기 생성 API** : 뿌릴총인원 중 1명을 제외하고는 모두 (뿌릴 금액 / 뿌릴 총인원)의 금액을 받게 되고, 남은 1명은 (뿌릴 금액 / 뿌릴 총인원 + 뿌릴 금액 % 뿌릴 총인원)의 금액을 받게 되도록 분배 로직을 설정하였습니다.
- **머니뿌리기 조회 API** 
- **머니 받기 API**

#### member 모듈 : 머니뿌리기 API 동작을 위한 회원 생성 및 조회 용도
- **회원 생성 API** : 회원 아이디는 회원 이름으로 가정합니다.
- **회원 조회 API**

#### account 모듈  : 머니뿌리기 API 동작을 위한 계좌 생성, 계좌 조회, 입금, 계좌이체 용도
- **계좌 생성 API** : 계좌번호는 계좌의 Id(primary key)로 사용된다고 가정합니다.
- **계좌 조회 API**
- **입금 API**
- **계좌이체 API** : 뿌리기에 충분한 잔액을 보유하고 있다고 가정하며, 잔액에 관련된 검증 로직은 생략합니다. (테스트 코드 상에서는 실제로 계좌이체 전에 충분한 금액을 입금하도록 입금 API 호출 후 테스트 진행)

#### chatroom 모듈 : 머니뿌리기 API 동작을 위한 채팅방 생성 용도
- **채팅방 생성 API** : 채팅방 아이디는 랜덤한 알파벳 10자리로 생성됩니다. 채팅방은 생성될 때 모든 인원이 들어온다고 가정하며, 채팅방 엔티티의 PK값을 채팅방 아이디로 가정합니다.
<br>


