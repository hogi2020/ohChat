package ojmChat;

import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerRoomMsg {
    // 선언부
    private String roomName;
    CopyOnWriteArrayList<ObjectOutputStream> outStreams;

    // 생성자
    public ServerRoomMsg (String roomName) {
        this.roomName = roomName;
        this.outStreams = new CopyOnWriteArrayList<>();
    }


}
