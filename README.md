# OhChat! - 실시간 채팅 프로그램


## 📌 프로젝트 소개
OhChat!은 Java Swing을 활용한 실시간 멀티룸 채팅 프로그램입니다.
소켓 통신과 멀티스레드를 활용하여 다수의 사용자가 동시에 채팅방을 생성하고 대화할 수 있습니다.

</br>

## 🛠 주요 기능
**1. 회원 관리**
- 회원가입/로그인
- 닉네임 변경
- 회원 탈퇴
- IP 기반 중복 가입 방지

</br>

**2. 채팅방 관리**
- 실시간 채팅방 생성
- 채팅방 입장/퇴장
- 실시간 메시지 송수신
- 채팅방 목록 실시간 업데이트

</br>

## 🏗 시스템 아키텍처
**클라이언트 (ojmChat)**
- ClientUI: 메인 채팅 인터페이스
- ClientLoginUI: 로그인 화면
- ClientJoinUI: 회원가입 화면
- ClientProtocol: 서버와의 통신 처리

</br>

**서버 (ojmChat)**
- ServerMain: 서버 시작 및 클라이언트 연결 관리
- ServerThread: 클라이언트별 요청 처리
- ServerDataMng: 데이터 및 비즈니스 로직 관리

</br>

**데이터베이스 (ojmDB)**
- DBConnectionMgr: 커넥션 풀 관리
- MemberDAO: 회원 정보 관리
- ChatRoomDAO: 채팅방 정보 관리
- MessageDAO: 채팅 메시지 관리

</br>

## 💻 기술 스택
- 언어: Java
- UI: Java Swing
- 데이터베이스: Oracle
- 통신: Socket, ObjectStream
- 동시성 처리: Thread, ConcurrentHashMap

</br>

## 🔍 주요 특징
- 커넥션 풀링(Connection pooling)
- 데이터베이스 연결 효율적 관리
- 동시 접속자 처리 최적화
- 동시성 처리
- ConcurrentHashMap을 활용한 스레드 안전성 보장
- 멀티스레드 환경에서 안정적인 데이터 처리
- 실시간 업데이트
- 채팅방 목록 실시간 동기화
- 메시지 즉시 전달 시스템

</br>

## 🚀 시작하기
**1. Oracle 데이터베이스 설정**
```sql
-- 필요한 테이블 생성 스크립트 실행
CREATE TABLE member (...)
CREATE TABLE talk_room (...)
CREATE TABLE message (...)
```

**2. 서버 실행 (Main)** 
> ServerMain.java

**3. 클라이언트 실행 (Main)**
> ClientLoginUI.java

</br>

## 📋 프로젝트 구조
```
 src/ </br>
 ├── ojmChat/           # 채팅 관련 클래스
 │   ├── Client*.java   # 클라이언트 관련 클래스
 │   └── Server*.java   # 서버 관련 클래스
 │  
 └── ojmDB/             # 데이터베이스 관련 클래스
     ├── *DAO.java      # 데이터 접근 인터페이스
     └── *DAO_Im.java   # 데이터 접근 구현체
```

</br>

## 🔒 보안 기능
- 비밀번호 검증 시스템
- IP 기반 중복 가입 방지
- 세션 관리를 통한 접근 제어

</br>

## 🔄 프로토콜
- MsgSend: 메시지 전송
- Create: 채팅방 생성
- Enter: 채팅방 입장
- Join: 회원가입
- Update: 회원정보 수정
- Delete: 회원 탈퇴
- LoginCheck: 로그인 검증

</br>

## 📝 향후 개선 사항
- 메시지 암호화 도입
- 파일 전송 기능 추가
- 이모티콘 지원
- 채팅방 권한 관리 시스템
- UI/UX 개선

</br>

## UML Image

