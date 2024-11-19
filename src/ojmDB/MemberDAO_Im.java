package ojmDB;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemberDAO_Im implements MemberDAO {
    private final DBConnectionMgr dbMgr;
    private Connection conn;
    private PreparedStatement pstmt;
    private CallableStatement cstmt;
    private ResultSet rs;

    // 생성자
    public MemberDAO_Im() {
        // this를 굳이 사용하는 이유는 멤버변수임을 명시적으로 표현하기 위함
        this.dbMgr = DBConnectionMgr.getInstance();
    }


    @Override
    public void insertMem(String mem_ip, String mem_nick, String mem_pw) {
        String sql = "insert into member values (?,?,?,?)";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, mem_ip);
            pstmt.setString(2, mem_nick);
            pstmt.setString(3, null);
            pstmt.setString(4, mem_pw);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("insertMem Exception : " + e.getMessage());
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        }
    }


    @Override
    public void updateMem(String mem_ip, String update_nick, String update_pw) {
        String sql = "update member set mem_nick_change = mem_nick, mem_nick = ? where mem_ip = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, update_nick);
            pstmt.setString(2, mem_ip);
            pstmt.setString(3, update_pw);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("updateMem Exception : " + e.getMessage());
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        }
    }


    @Override
    public void deleteMem(String mem_nick, String mem_pw) {
        String sql = "delete from member where mem_nick = ? and mem_pw = ?";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, mem_nick);
            pstmt.setString(2, mem_pw);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("deleteMem Exception : " + e.getMessage());
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        }
    }


    @Override
    public int loginCheck(String nickName, int pw) {
        int tfNum = -1;

        try {
            // 1. 데이터베이스 연결
            conn = dbMgr.getConnection();

            // 2. CallableStatement 객체 생성 (프로시저 호출)
            cstmt = conn.prepareCall("call CHAT_JOIN(?,?,?)");

            // 3. 프로시저의 파라미터 설정
            cstmt.setString(1, nickName);
            cstmt.setInt(2, pw);
            cstmt.registerOutParameter(3, Types.INTEGER);

            // 4. 프로시저 실행 & 프로시저 결과 받기
            cstmt.execute();
            tfNum = cstmt.getInt(3);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbMgr.freeConnection(conn, cstmt);
        }
        return tfNum;
    }


    @Override
    public Map<String, String> getAllMem() {
        Map<String, String> memMap = new ConcurrentHashMap<>();
        String sql = "select * from member";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                memMap.put(rs.getString("mem_ip"), rs.getString("mem_nick"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}

        return memMap;
    }


    @Override
    public List<String> getJoinMemList(String roomName) {
        List<String> joinMemList = new CopyOnWriteArrayList<>();
        String sql = "select mem_nick from room_member rm " +
                        "join talk_room tr on rm.talk_room_id = tr.talk_room_id " +
                        "join member mem on rm.mem_ip = mem.mem_ip " +
                        "where tr.talk_room_name = ?";
        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                joinMemList.add(rs.getString("mem_nick"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}

        return joinMemList;
    }


    @Override
    public String getMemIP(String nickName) {
        String sql = "select mem_ip from member where mem_nick = ?";
        String mem_ip = null;

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                mem_ip = rs.getString("mem_ip");
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}

        return mem_ip;
    }
}
