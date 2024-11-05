package ojmDB;

import ojmDB.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTestCode {
    DBConnection dbConn = DBConnection.dbMgr.getInstance();
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public void insertSQL() {
        conn = dbConn.getConnection();

        String sql = "select * from member";
        try {
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {


    }

}
