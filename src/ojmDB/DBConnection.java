package ojmDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {

    // Oracle DB Connection info
    public final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
    public final String _URL = "jdbc:oracle:thin:@localhost:1521:orcl11";
    public final String _USER = "scott";
    public final String _PW = "tiger";

    // Connection Class
    static DBConnection dbMgr;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    // 생성자 생성
    public DBConnection() {}

    // DBConnection 타입의 변수를 선언
    // DBConnection 클래스에 대한 싱글톤 패턴 구현
    public static DBConnection getInstance() {
        // if문의 조건절에서 null 체크함.
        if(dbMgr == null) {
            dbMgr = new DBConnection();
        }
        return dbMgr;
    }

    // Database 연동 메서드 생성
    public Connection getConnection() {
        try {
            // Class.forName()은 드라이버를 JVM에 로드하는 역할을 합니다.
            // 즉 JDBC 클래스가 정적 블록에 실행되어 DB연결을  초기화하는 역할을 합니다.
            Class.forName(_DRIVER);
            conn = DriverManager.getConnection(_URL, _USER, _PW);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
