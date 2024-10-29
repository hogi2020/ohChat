package ojmChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OjmClientHandler implements Runnable {
    // 선언부
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    OjmServer os;

    // 생성자
    public OjmClientHandler(Socket socket, OjmServer os) {
        this.socket = socket;
        this.os = os;
    }

    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("입출력 스트림 객체 생성 | " + socket);

            // 클라이언트와 Room을 추가하고, Room 리스트를 문자열로 전송
            os.addClientToRoom(out, "");
            os.broadcastRoomList();

            while (true) {
                String message = (String) in.readObject();
                String[] parts = message.split("#", 2);

                if (parts.length == 2) {
                    String command = parts[0];
                    String content = parts[1];

                    OjmChatRoom room = os.getChatRoom(content);

                    switch (command) {
                        case "Create":
                            os.createRoom(content);
                            break;
                        case "Join":
                            room.addClient(out);
                            os.addClientToRoom(out, content);
                            out.writeObject("JOINED:" + content);
                            break;
                        default:
                            String currentRoom = os.getClientRoom(out);
                            if (currentRoom != null) {
                                os.getChatRoom(currentRoom);
                                room.broadcast(message, out);
                            }
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("클라이언트 처리 중 오류 발생: " + e.getMessage());
        } finally {
            String roomName = os.getClientRoom(out);
            if (roomName != null) {
                os.getChatRoom(roomName).removeClient(out);
            }
            os.removeClientFromRoom(out);
        }
    }
}
