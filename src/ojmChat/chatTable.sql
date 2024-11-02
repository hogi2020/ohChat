CREATE DATABASE chat_db;
USE chat_db;

CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- 주요 기능:
-- 1. 데이터베이스 연결/종료 관리
-- 2. 채팅 메시지 저장
-- 3. 채팅방별 히스토리 조회

-- 사용하기 전에 다음 사항을 확인하세요:
-- 1. MySQL JDBC 드라이버 추가 (pom.xml 또는 직접 라이브러리 추가)
-- 2. 데이터베이스 접속 정보 (URL, USER, PASSWORD) 수정
-- 3. 데이터베이스와 테이블 생성

-- 이 코드를 실제로 사용하려면 ServerThread.java의 메시지 처리 부분에서 
-- DBManager를 통해 메시지를 저장하도록 수정해야 합니다.