package ojmDB;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoomDAO_Im implements ChatRoomDAO {
    private final DBConnectionMgr dbMgr;
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public ChatRoomDAO_Im() {
        this.dbMgr = DBConnectionMgr.getInstance();
    }


    @Override
    public int insertRoom(String roomName) {
        int tfNum = -1;
        String[] sqls = {
                "select count(*) from talk_room where talk_room_name = ?",
                "insert into talk_room values (talk_room_seq.nextval,?)"};
        try {
            conn = dbMgr.getConnection();

            // 1. 그룹창 중복 체크
            pstmt = conn.prepareStatement(sqls[0]);
            pstmt.setString(1, roomName);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                tfNum = 0;
            } else {
                // 2. 그룹창 추가
                pstmt.close();  // 기존 pstmt 제거
                pstmt = conn.prepareStatement(sqls[1]);
                pstmt.setString(1, roomName);
                tfNum = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();    // 오류를 콘솔에 출력하여 추적
        } finally {
            dbMgr.freeConnection(conn, pstmt, rs);
        }
        return tfNum;
    }


    @Override
    public void enterRoom(String nickName, String roomName) {
        int tfNum = -1;
        String sql = "insert into room_member values (" +
                    "(select mem_ip from member where mem_nick = ?), " +
                    "(select talk_room_id from talk_room where talk_room_name = ?))";
        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);

            // DB 내 데이터 저장
            pstmt.setString(1, nickName);
            pstmt.setString(2, roomName);
            tfNum = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("insertRoom Exception : " + e.getMessage());
        } finally {
            dbMgr.freeConnection(conn, pstmt);
        } // 자원 명시적 종료
    }


    @Override
    public Map<String, String> getRoomMap() {
        Map<String, String> roomMap = new ConcurrentHashMap<>();
        String sql = "select * from talk_room";

        try {
            conn = dbMgr.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                roomMap.put(rs.getString("talk_room_id"), rs.getString("talk_room_name"));
            }
        } catch (SQLException e) { e.printStackTrace();
        } finally { dbMgr.freeConnection(conn, pstmt, rs);}
        return roomMap;
    }
}
