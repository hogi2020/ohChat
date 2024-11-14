# OhChat! (채팅 애플리케이션)
Project for Java, PL/SQL, Oracle DB

---

## **프로젝트 개요**
Java Swing을 이용한 멀티룸 채팅 애플리케이션입니다.  
사용자는 로그인 후 여러 채팅방을 생성하고 참여할 수 있습니다.

## **주요 기능**
### **1. 사용자 관리**
- 회원가입/로그인
- 닉네임 수정
- 회원 탈퇴

### **2. 채팅방 기능**
- 새로운 채팅방 생성
- 기존 채팅방 입장
- 실시간 메시지 전송
- 채팅방 목록 실시간 업데이트

## **시스템 구조**
### **1. 서버 사이드**
- `ServerMain`: 서버 실행
- `ServerThread`: 클라이언트 연결 관리 및 메시지 처리
- `ServerDataMng`: 채팅방, 사용자, 메시지 데이터 관리
- `ProjectDAO`: 데이터베이스 연동 처리

### **2. 클라이언트 사이드**
- `ClientLoginUI`: 로그인 화면
- `ClientJoinUI`: 회원가입 화면
- `ClientUI`: 메인 채팅 인터페이스
- `ClientProtocol`: 서버-클라이언트 통신 처리

## **기술 스택**
- 언어: Java, PL/SQL
- UI: Java Swing
- 네트워크: Socket 통신
- 동시성 처리: ConcurrentHashMap, CopyOnWriteArrayList
- 데이터 저장: JDBC (OrcleDB)

## **통신 프로토콜**
- `MsgSend`: 채팅 메시지 전송
- `Create`: 새로운 채팅방 생성
- `Enter`: 채팅방 입장
- `Join`: 회원가입
- `Update`: 회원정보 수정
- `Delete`: 회원 탈퇴
- `LoginCheck`: 로그인 인증

## **애플리케이션 주요 특징**
- 멀티스레드 기반의 동시 접속 지원
- 실시간 채팅방 목록 업데이트
- 채팅 기록 데이터베이스 저장 및 호출
- 심플한 GUI 인터페이스

## UML Image
![diagram-6430462117493576377](https://github.com/user-attachments/assets/cf3bdadc-b944-4a8e-8ebe-18f098647d03)

