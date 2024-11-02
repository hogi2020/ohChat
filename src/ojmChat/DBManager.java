package ojmChat;

import java.sql.*;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/chat_db?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    
    private Connection conn;
    
    // 데이터베이스 연결
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("데이터베이스 연결 성공");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 실패: " + e.getMessage());
        }
    }
    
    // 채팅 메시지 저장
    public void saveMessage(String roomName, String message) {
        String sql = "INSERT INTO chat_messages (room_name, message, created_at) VALUES (?, ?, NOW())";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomName);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("메시지 저장 실패: " + e.getMessage());
        }
    }
    
    // 채팅방 히스토리 조회
    public ResultSet getChatHistory(String roomName) {
        String sql = "SELECT message, created_at FROM chat_messages WHERE room_name = ? ORDER BY created_at DESC LIMIT 100";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("채팅 히스토리 조회 실패: " + e.getMessage());
            return null;
        }
    }
    
    // 데이터베이스 연결 종료
    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("데이터베이스 연결 종료");
            }
        } catch (SQLException e) {
            System.out.println("데이터베이스 종료 실패: " + e.getMessage());
        }
    }
} 