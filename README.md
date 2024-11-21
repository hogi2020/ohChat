# OhChat! - 실시간 채팅 프로그램


## 📌 프로젝트 소개
OhChat!은 Java Swing을 활용한 실시간 멀티룸 채팅 프로그램입니다.
소켓 통신과 멀티스레드를 활용하여 다수의 사용자가 동시에 채팅방을 생성하고 대화할 수 있습니다.

## 🛠 주요 기능
1. 회원 관리
- 회원가입/로그인
- 닉네임 변경
- 회원 탈퇴
- IP 기반 중복 가입 방지

2. 채팅방 관리
- 실시간 채팅방 생성
- 채팅방 입장/퇴장
- 실시간 메시지 송수신
- 채팅방 목록 실시간 업데이트

## 🏗 시스템 아키텍처
**클라이언트 (ojmChat)**
ClientUI: 메인 채팅 인터페이스
ClientLoginUI: 로그인 화면
ClientJoinUI: 회원가입 화면
ClientProtocol: 서버와의 통신 처리
서버 (ojmChat)
ServerMain: 서버 시작 및 클라이언트 연결 관리
ServerThread: 클라이언트별 요청 처리
ServerDataMng: 데이터 및 비즈니스 로직 관리
데이터베이스 (ojmDB)
DBConnectionMgr: 커넥션 풀 관리
MemberDAO: 회원 정보 관리
ChatRoomDAO: 채팅방 정보 관리
MessageDAO: 채팅 메시지 관리
💻 기술 스택
언어: Java
UI: Java Swing
데이터베이스: Oracle
통신: Socket, ObjectStream
동시성 처리: Thread, ConcurrentHashMap
🔍 주요 특징
커넥션 풀링
데이터베이스 연결 효율적 관리
동시 접속자 처리 최적화
동시성 처리
ConcurrentHashMap을 활용한 스레드 안전성 보장
멀티스레드 환경에서 안정적인 데이터 처리
실시간 업데이트
채팅방 목록 실시간 동기화
메시지 즉시 전달 시스템
🚀 시작하기
Oracle 데이터베이스 설정


## UML Image
![diagram-6430462117493576377](https://github.com/user-attachments/assets/cf3bdadc-b944-4a8e-8ebe-18f098647d03)

