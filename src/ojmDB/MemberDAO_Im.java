package ojmDB;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemberDAO_Im implements MemberDAO {
    private final DBConnectionMgr dbMgr;

    // 생성자
    public MemberDAO_Im() {
        // this를 굳이 사용하는 이유는 멤버변수임을 명시적으로 표현하기 위함
        this.dbMgr = DBConnectionMgr.getInstance();
    }

    @Override
    public void insertMem(String mem_ip, String mem_nick, String mem_pw) {
        String sql = "insert into member values (?,?,?,?)";

        try {
            Connection conn = dbMgr.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, mem_ip);
            pstmt.setString(2, mem_nick);
            pstmt.setString(3, null);
            pstmt.setString(4, mem_pw);

            pstmt.executeUpdate();
            dbMgr.freeConnection(conn, pstmt);

        } catch (SQLException e) {
            System.out.println("insertMem Exception : " + e.getMessage());
        }
    }

    @Override
    public void updateMem(String mem_ip, String update_nick, String update_pw) {
        String sql = "update member set mem_nick_change = mem_nick, mem_nick = ? where mem_ip = ? and mem_pw = ?";

        try {
            Connection conn = dbMgr.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, update_nick);
            pstmt.setString(2, mem_ip);
            pstmt.setString(3, update_pw);

            pstmt.executeUpdate();
            dbMgr.freeConnection(conn, pstmt);

        } catch (SQLException e) {
            System.out.println("updateMem Exception : " + e.getMessage());
        }
    }

    @Override
    public void deleteMem(String mem_nick, String mem_pw) {
        String sql = "delete from member where mem_nick = ? and mem_pw = ?";

        try {
            Connection conn = dbMgr.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, mem_nick);
            pstmt.setString(2, mem_pw);

            pstmt.executeUpdate();
            dbMgr.freeConnection(conn, pstmt);

        } catch (SQLException e) {
            System.out.println("deleteMem Exception : " + e.getMessage());
        }
    }

    @Override
    public int loginCheck(String nickName, String pw) {
        int tfNum = -1;

        try {
            // 1. 데이터베이스 연결
            Connection conn = dbMgr.getConnection();

            // 2. CallableStatement 객체 생성 (프로시저 호출)
            CallableStatement cstmt = conn.prepareCall("call CHAT_JOIN(?,?,?)");

            // 3. 프로시저의 파라미터 설정
            cstmt.setString(1, nickName);
            cstmt.setInt(2, Integer.parseInt(pw));
            cstmt.registerOutParameter(3, Types.INTEGER);

            // 4. 프로시저 실행 & 프로시저 결과 받기
            cstmt.execute();
            tfNum = cstmt.getInt(3);
            dbMgr.freeConnection(conn, cstmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tfNum;
    }

    @Override
    public ConcurrentHashMap<String, String> getAllMem() {
        return null;
    }

    @Override
    public String getIP(String nickName) {
        return "";
    }
}
