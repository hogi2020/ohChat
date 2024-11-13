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
    ConcurrentHashMap<String, ServerRoomMsg> chatRoomMap;
    ConcurrentHashMap<ObjectOutputStream, String> clientRoomMap;


    ProjectDAO dbMgr = null;
    ConcurrentHashMap<String, CopyOnWriteArrayList<ObjectOutputStream>> roomMsgMap;    // RoomName, ClientList
    ConcurrentHashMap<String, ObjectOutputStream> clientInfoMap;    // nickName, OutputStream
    ConcurrentHashMap<String, String> roomMap;          // Roomid, RoomName


    // 생성자
    public ServerDataMng(ProjectDAO dbMgr) {
        chatRoomMap = new ConcurrentHashMap<>();
        clientRoomMap = new ConcurrentHashMap<>();

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
    }


    public void enterRoom(String groupName, String nickName) {
        dbMgr.joinGroup(nickName, groupName);
        CopyOnWriteArrayList<String> joinMemList = dbMgr.getJoinMemList(nickName);
        CopyOnWriteArrayList<String> msgList = dbMgr.getMsgList(groupName);

        for (String nick : joinMemList) {
            try {
                clientInfoMap.get(nick).writeObject("Reset#");
                clientInfoMap.get(nick).writeObject("MsgSend#>>[" + groupName + "]에 입장하였습니다.");

                for (String msg : msgList) {
                    clientInfoMap.get(nick).writeObject("MsgSend#" + msg);
                }
            } catch (IOException e) {
                System.out.println("DataMng-enterRoom 에러 발생 | " + e.getMessage());
            }
        }
    }

    public void broadcastMsg(String roomName) {
        for (ObjectOutputStream outStream : clientRoomMap.keySet()) {
            try {
                outStream.writeObject("Reset#");
                outStream.writeObject("MsgSend#>>["+roomName+"]에 입장하였습니다.");
            } catch (IOException e) {
                System.out.println("DataMng-broadcastMsg 에러 발생 | " + e.getMessage());
            }
        }
    }

}
