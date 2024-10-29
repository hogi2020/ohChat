package ojmChat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class OjmServer {
    // 선언부
    final int PORT = 3000;
    ConcurrentHashMap<String, OjmChatRoom> chatRooms;           // 그룹창 Map
    ConcurrentHashMap<ObjectOutputStream, String> clientRooms;  // 클라이언트 Map


    // 생성자
    public OjmServer() {

        chatRooms = new ConcurrentHashMap<>();
        clientRooms = new ConcurrentHashMap<>();
    }

    // 서버 실행 및 스레드 관리
    public void start() {
        // 서버 포트 지정
        try(ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Ready to Server.....");

            // 클라이언트 소켓 Accept() 반복문
            while (true) {
                // 클라이언트가 들어올때까지 accept()에서 계속 블로킹!
                Socket clientSocket = ss.accept();
                System.out.println("Access Client | " + clientSocket.getInetAddress());
                new Thread(new OjmClientHandler(clientSocket, this)).start();
            }
        } catch (IOException e) {
            System.out.println("서버 작동 중 오류 발생: " + e.getMessage());
        }
    }

    // chatRooms Object 호출
    public OjmChatRoom getChatRoom(String roomName) {
        return chatRooms.computeIfAbsent(roomName, k -> new OjmChatRoom(roomName));
    }

    // 그룹창 생성
    public void createRoom(String roomName) {
        if (!chatRooms.containsKey(roomName)) {
            chatRooms.put(roomName, new OjmChatRoom(roomName));
            broadcastRoomList();
        }
    }

    // RoomList를 해당 클라이언트에게 전송
    public void broadcastRoomList() {
        // RoomList 결과는 아래와 같이 출력됩니다.
        // ROOMLIST: room1, room2, room3
        String roomList = "RoomList:" + String.join(",", chatRooms.keySet());
        System.out.println(roomList);   // test
        for (ObjectOutputStream client : clientRooms.keySet()) {
            try {
                client.writeObject(roomList);
                client.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("broadcast 진행");
        }
    }


    // HashMap에 그룹명 추가하기
    public void addClientToRoom(ObjectOutputStream client, String roomName) {
        clientRooms.put(client, roomName);
    }

    public void removeClientFromRoom(ObjectOutputStream client) {
        clientRooms.remove(client);
    }

    public String getClientRoom(ObjectOutputStream client) {
        return clientRooms.get(client);
    }


    // 메인 스레드 싱행
    public static void main(String[] args) {
        new OjmServer().start();
    }
}