package ojmChat;

import ojmDB.ProjectDAO;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ServerDataMng {
    // 선언부 | Variable
    private int password;

    // 선언부 | Class
    ProjectDAO pDAO;
    ConcurrentHashMap<String, List<ObjectOutputStream>> roomMsgMap;    // RoomName, ClientList
    ConcurrentHashMap<String, ObjectOutputStream> clientInfoMap;    // nickName, OutputStream
    ConcurrentHashMap<String, String> roomMap;          // Roomid, RoomName


    // 생성자
    public ServerDataMng(ProjectDAO pDAO) {
        // 인스턴스화
        this.pDAO = pDAO;
        roomMsgMap = new ConcurrentHashMap<>();
        clientInfoMap = new ConcurrentHashMap<>();
    } /////////////// ServerDataMng


    /// Database 관련 메서드 집합 ///
    public int loginCheck(String nickName, String pw, ObjectOutputStream outStream) {
        // 로그인 성공 시 1 반환, 실패 시 0 반환
        password = Integer.parseInt(pw);
        if(pDAO.loginCheck(nickName, password) == 1) {
            // 로그인에 성공하면 ClientList에 Outstream 추가
            clientInfoMap.put(nickName, outStream);
            return 1;
        } else {
            return 0;
        }
    }////////////////// loginCheck


    public String getIP(String nickName) {
         return pDAO.getIP(nickName);
    }////////////////// getIP


    public int createRoom(String mem_ip, String nickName, String roomName, String createMsg) {
        // 그룹 생성 | 중복이 아니면 1, 중복이면 0 반환
        if (pDAO.insertGroup(roomName) == 1) {
            roomMsgMap.put(roomName, new CopyOnWriteArrayList<>());
            pDAO.joinGroup(nickName, roomName);
            pDAO.insertMsg(createMsg, mem_ip, roomName);
            return 1;
        } else {
            return 0;
        }
    }/////////////////// createRoom


    public void broadcastRoomList(String protocolName) {
        Collection<String> roomList = pDAO.getRoomMap().values();
        String roomListStr = roomList.stream().collect(Collectors.joining(","));

        try  {
            for (String key : clientInfoMap.keySet()) {
                clientInfoMap.get(key).writeObject(protocolName + roomListStr);
                clientInfoMap.get(key).flush();
            }
        } catch (IOException e) {
            System.out.println("broadcastRoomList 에러 발생 | " + e.getMessage());
        }
    }/////////////////// broadcastRoomList


    public void enterRoom(String nickName, String roomName) {
        pDAO.joinGroup(nickName, roomName);
    }/////////////////// enterRoom


    public void saveMsg(String msg, String mem_ip, String roomName) {
        pDAO.insertMsg(msg, mem_ip, roomName); // 메세지 저장
    }/////////////////// saveMsg


    public void broadcastMsg(String protocolName, String roomName) {
        List<String> joinMemList = pDAO.getJoinMemList(roomName);  // 그룹에 입장한 회원리스트
        List<String> msgList = pDAO.getMsgList(roomName);          // 그룹에 저장된 메세지리스트

        for (Object nick : joinMemList) {
            if (clientInfoMap.containsKey(nick)) {
                try {
                    clientInfoMap.get(nick).writeObject("Reset#");
                    for (String msg : msgList) {
                        clientInfoMap.get(nick).writeObject(protocolName + msg);
                    }
                } catch (IOException e) {
                    System.out.println("broadcastMsg 에러 발생 | " + e.getMessage());
                }
            }
        }
    }/////////////////// broadcast


    public int crudSQL (String command, String mem_ip, String nickName, String pw) {
        switch (command) {
            case "insert":
                pDAO.insertMem(mem_ip, nickName, pw);
                return 1;
            case "delete":
                pDAO.deleteMem(nickName, pw);
                return 1;
            case "update":
                pDAO.updateMem(mem_ip, nickName, pw);
                return 1;
            default:
                return 0;
        }
    }
}
