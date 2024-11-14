package ojmChat;

import ojmDB.ProjectDAO;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ServerDataMng {
    // 선언부
    ProjectDAO dbMgr = null;
    ConcurrentHashMap<String, CopyOnWriteArrayList<ObjectOutputStream>> roomMsgMap;    // RoomName, ClientList
    ConcurrentHashMap<String, ObjectOutputStream> clientInfoMap;    // nickName, OutputStream
    ConcurrentHashMap<String, String> roomMap;          // Roomid, RoomName


    // 생성자
    public ServerDataMng(ProjectDAO dbMgr) {
        // 인스턴스화
        this.dbMgr = dbMgr;
        roomMsgMap = new ConcurrentHashMap<>();
        clientInfoMap = new ConcurrentHashMap<>();
    }


    /// Database 관련 메서드 집합 ///
    public void broadcastRoomList() {
        // roomList
        Collection<String> roomList = dbMgr.getRoomMap().values();
        String roomListStr = roomList.stream().collect(Collectors.joining(","));

        try  {
            for (String key : clientInfoMap.keySet()) {
                clientInfoMap.get(key).writeObject("RoomList#" + roomListStr);
                clientInfoMap.get(key).flush();
            }
        } catch (IOException e) {
            System.out.println("DataMng-broadcast 에러 발생 | " + e.getMessage());
        }
    }/////////////////// broadcastRoomList


    public void enterRoom(String groupName, String nickName) {
        dbMgr.joinGroup(nickName, groupName);

        CopyOnWriteArrayList<String> joinMemList = dbMgr.getJoinMemList(groupName);
        CopyOnWriteArrayList<String> msgList = dbMgr.getMsgList(groupName);

        for (String nick : clientInfoMap.keySet()) {
            if (joinMemList.contains(nick)) {
                try {
                    clientInfoMap.get(nick).writeObject("Reset#");
                    System.out.println("nickOutstreamInfo | " + clientInfoMap.get(nick));

                    for (String msg : msgList) {
                        clientInfoMap.get(nick).writeObject("MsgSend#" + msg);
                    }
                } catch (IOException e) {
                    System.out.println("DataMng-enterRoom 에러 발생 | " + e.getMessage());
                }
            }
        }
    }/////////////////// enterRoom


    public void broadcastMsg(String msg, String mem_ip, String roomName) {

        dbMgr.insertMsg(msg, mem_ip, roomName); // 메세지 저장
        CopyOnWriteArrayList<String> joinMemList = dbMgr.getJoinMemList(roomName);  // 그룹에 입장한 회원리스트
        CopyOnWriteArrayList<String> msgList = dbMgr.getMsgList(roomName);          // 그룹에 저장된 메세지리스트

//        System.out.println("content | " + content);
//        System.out.println("mem_ip | " + mem_ip);
//        System.out.println("roomName | " + roomName);
//        System.out.println("joinMemList | " + joinMemList);
//        System.out.println("msgList | " + msgList);

        for (String nick : clientInfoMap.keySet()) {
            if (joinMemList.contains(nick)) {
                try {
                    clientInfoMap.get(nick).writeObject("Reset#");
                    for (String c_msg : msgList) {
                        clientInfoMap.get(nick).writeObject("MsgSend#" + c_msg);
                    }
                } catch (IOException e) {
                    System.out.println("DataMng-broadcastMsg 에러 발생 | " + e.getMessage());
                }
            }
        }
    }/////////////////// broadcastMsg
}
