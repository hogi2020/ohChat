package ojmChat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDataMng {
    // 선언부
    ConcurrentHashMap<String, ServerRoomMsg> chatRoomMap;
    ConcurrentHashMap<ObjectOutputStream, String> clientRoomMap;

    // 생성자
    public ServerDataMng() {
        chatRoomMap = new ConcurrentHashMap<>();
        clientRoomMap = new ConcurrentHashMap<>();
    }

    // ClientRoomMap에 클라이언트 추가
    public void addClientToRoom(ObjectOutputStream clientOutStream, String roomName) {
        clientRoomMap.put(clientOutStream, roomName);
    }

    // 그룹창 생성 메서드
    public void createRoom(String roomName) {
        if (!chatRoomMap.containsKey(roomName)) {
            chatRoomMap.put(roomName, new ServerRoomMsg(roomName));
        }
    }


    // RoomList -> 클라이언트 전송
    public void broadcastRoomList() {
        String roomList = "RoomList#" + String.join(",", chatRoomMap.keySet());

        for (ObjectOutputStream outStream : clientRoomMap.keySet()) {
            try {
                outStream.writeObject(roomList);
                outStream.flush();
            } catch (IOException e) {
                System.out.println("DataMng-broadcast 에러 발생 | " + e.getMessage());
            }
        }
    }
}
