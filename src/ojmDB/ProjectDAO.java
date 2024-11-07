package ojmDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectDAO {
    DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
    Connection conn;
    PreparedStatement pstmt;
    String sql = new String();
    int tfNum = -1;     // 성공은 1, 실패는 0


    // Database 데이터 입력 메서드
    public void insertMem(String mem_ip, String mem_nick, String mem_pw) {
        sql = "insert into member values (?,?,?)";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 저장
            pstmt.setString(1, mem_ip);
            pstmt.setString(2, mem_nick);
            pstmt.setString(3, mem_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) { this.tfNum = -1;
        } finally { dbMgr.freeConnection(conn, pstmt);} // 자원 명시적 종료
    }


    // Database 데이터 삭제 메서드
    public void deleteMem(String mem_nick, String mem_pw) {
        sql = "delete from member where mem_nick = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 삭제
            pstmt.setString(1, mem_nick);
            pstmt.setString(2, mem_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt);} // 자원 명시적 종료
    }


    // UPDATE [테이블] SET [열] = '변경할값' WHERE [조건]
    // Database 데이터 수정 메서드
    public void updateMem(String mem_ip, String update_nick, String update_pw) {
        sql = "update member set mem_nick = ? where mem_ip = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 삭제
            pstmt.setString(1, update_nick);
            pstmt.setString(2, mem_ip);
            pstmt.setString(3, update_pw);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) { this.tfNum = -1;
        } finally { dbMgr.freeConnection(conn, pstmt);} // 자원 명시적 종료
    }

    // 결과 출력
    public int result() {
        return tfNum;
    }
}
