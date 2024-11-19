package ojmDB;

import java.util.Map;

public interface ChatRoomDAO {

    int insertRoom(String roomName);
    void enterRoom(String nickName, String roomName);
    Map<String, String> getRoomMap();
}
