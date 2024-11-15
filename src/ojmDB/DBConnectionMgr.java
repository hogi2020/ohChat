package ojmDB;

import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DBConnectionMgr {
    // Connection Class
    private static DBConnectionMgr dbMgr;
    private BlockingQueue<Connection> connectionPool;

    // pool 설정
    private final int INIT_POOL_SIZE = 5;
    private final int MAX_POOL_SIZE = 20;

    // Oracle DB Connection info
    public final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
    public final String _URL = "jdbc:oracle:thin:@localhost:1521:orcl11";
    public final String _USER = "scott";
    public final String _PW = "tiger";


    // 생성자 설정
    private DBConnectionMgr() {
        try {
            Class.forName(_DRIVER);
            connectionPool = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패" + e.getMessage());
        }
    }

    // 커넥션 생성
    private Connection createNewConn() {
        try {
            return DriverManager.getConnection(_URL, _USER, _PW);
        } catch (SQLException e) {
            System.out.println("커넥션 생성 실패" + e.getMessage());
            return null;
        }
    }


    // Pool 초기화 선언
    private void initializeConnPool() throws SQLException {
        for (int i = 0; i < INIT_POOL_SIZE; i++) {
            connectionPool.offer(createNewConn());
        }
    }


    // DBConnection 타입의 변수를 선언
    // DBConnection 클래스에 대한 싱글톤 패턴 구현
    public static DBConnectionMgr getInstance() {
        // if문의 조건절에서 null 체크함.
        if(dbMgr == null) { dbMgr = new DBConnectionMgr(); }
        return dbMgr;
    }


    // Database 연동 메서드 생성
    public Connection getConnection() {
        try {
            Connection conn = connectionPool.poll();
            if (conn == null || conn.isClosed()) {
                if (connectionPool.size() < MAX_POOL_SIZE) {
                    conn = createNewConn();
                }  else {
                    conn = connectionPool.take(); // 대기
                }
            }
            return conn;
        } catch (InterruptedException | SQLException e) {
            System.out.println("커넥션 획득 중 인터럽트 발생 | " + e.getMessage());
            return null;
        }
    }


    private void releaseConn(Connection conn) {
        try {
            if (!conn.isClosed() && connectionPool.size() < MAX_POOL_SIZE) {
                connectionPool.offer(conn);
            }
        } catch (SQLException e) {
            System.out.println("커넥션 반환 중 오류 발생 | " + e.getMessage());
        }
    }


    // 사용한 자원 반납하기
    // 생략 시, JVM의 가비지컬렉터가 대신 하지만, 명시적으로 구현하는 것을 권장
    public void freeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if(pstmt != null) { pstmt.close();}
            if(rs != null) { rs.close();}
            if(conn != null) { releaseConn(conn);}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection conn, PreparedStatement pstmt) {
        try {
            if(pstmt != null) { pstmt.close();}
            if(conn != null) { releaseConn(conn);}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
