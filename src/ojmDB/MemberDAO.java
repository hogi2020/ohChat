package ojmDB;

import java.util.concurrent.ConcurrentHashMap;

public interface MemberDAO extends BaseDAO {

    // Database CRUD 메서드 //
    void insertMem(String mem_ip, String mem_nick, String mem_pw);
    void updateMem(String mem_ip, String update_nick, String update_pw);
    void deleteMem(String mem_nick, String mem_pw);

    // Database Select 메서드 //
    int loginCheck(String nickName, String pw);
    ConcurrentHashMap<String, String> getAllMem();
    String getIP(String nickName);

}