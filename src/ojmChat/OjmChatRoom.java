package ojmChat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class OjmChatRoom {
    // 선언부
    String name;
    /// ArrayList를 구현한 클래스로서, 배열 내부를 초기화하여 작업 수행
    /// 객체를 매번 복사하지 않고, 전달할 때 해당 상태를 스냅샷으로 가지고 있는 방식입니다.
    /// 동시성 제어 이슈를 최소화하기 위해 해당 클래스 사용!!!
    CopyOnWriteArrayList<ObjectOutputStream> outs;

    // 생성자
    /// 생성자를 통해 리스트 및 그룹명 인스턴스 가져오기
    public OjmChatRoom(String name) {
        this.name = name;
        this.outs = new CopyOnWriteArrayList<>();
    }

    // 클라이언트 입장 메소드
    /// 리스트에 해당 소켓의 출력 스트림을 저장
    public void addClient(ObjectOutputStream out) {
        outs.add(out);
        broadcast(name + ": 새로운 사용자가 입장했습니다.", null);
    }

    // 클라이언트 삭제 메소드
    /// 리스트 내 소켓의 출력 스트림을 제거
    public void removeClient(ObjectOutputStream out) {
        outs.remove(out);   // 인덱스 혹은 값을 통해 리스트 내 값 삭제
        broadcast(name + ": 사용자가 퇴장하였습니다.", null);
    }

    // 브로드 캐스트
    /// 리스트 내 출력 스트림들에 메세지 전송
    /// sender는 브로드캐스트를 사용할 리스트를 의미합니다.
    public void broadcast(String msg, ObjectOutputStream sender) {
        for (ObjectOutputStream out : outs) {
            if (out != sender) {
                try {
                    out.writeObject(msg);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
