package ojmChat;

import ojmDB.ProjectDAO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerThread implements Runnable {
    // 선언부
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    ServerDataMng sdm;
    ProjectDAO dbMgr;
    String mem_ip;
    String roomName;
    String[] strings;


    // 생성자 | 서버 소켓
    public ServerThread() {}
    public ServerThread(Socket socket, ServerDataMng sdm, ProjectDAO dbMgr) {
        this.clientSocket = socket;
        this.sdm = sdm;
        this.dbMgr = dbMgr;
        mem_ip = clientSocket.getInetAddress().getHostAddress();
    }


    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("입출력 Stream 객체 생성 | " + clientSocket);

            // RoomList 초기 호출
            sdm.broadcastRoomList();

            // 스레드 동작 처리
            while (true) {
                String msg = (String) inStream.readObject();
                System.out.println("스레드 동작 | " + msg);

                // 프로토콜 & 컨텐츠 분리
                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];


                // 프로토콜에 따른 서버 동작 실행
                switch(command) {
                    case "MsgSend":     /// 메세지 발송
                        dbMgr.insertMsg(content, mem_ip, roomName); // 메세지 저장
                        CopyOnWriteArrayList<String> joinMemList = dbMgr.getJoinMemList(roomName);  // 그룹에 입장한 회원리스트
                        CopyOnWriteArrayList<String> msgList = dbMgr.getMsgList(roomName);          // 그룹에 저장된 메세지리스트

                        try {
                            for (String nick : joinMemList) {
                                for (String cmsg : msgList) {
                                    sdm.clientInfoMap.get(nick).writeObject(cmsg);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("DataMng-MsgSend 에러 발생 | " + e.getMessage());
                        }
                        break;

                    case "Create":      /// 그룹창 생성
                        // 그룹 생성 및 중복 검사
                        if (dbMgr.insertGroup(content) == 0) {
                            outStream.writeObject("MsgGroup#동일한 그룹이 이미 존재합니다.");
                        } else {
                            // Map을 통해 각 그룹에 대한 클라이언트 리스트 생성
                            sdm.roomMsgMap.put(content, new CopyOnWriteArrayList<>());
                            sdm.broadcastRoomList();
                        }
                        break;

                    case "Enter":       /// 그룹창 입장
                        strings = content.split("/", 2);    // 그룹명과 회원명으로 분리
                        roomName = strings[0];                         // 현재 입장중인 그룹명
                        sdm.enterRoom(strings[0], strings[1]);         // 그룹에 입장 및 브로드캐스트
                        break;

                    case "Join":
                        strings = content.split("/", 2);
                        dbMgr.insertMem(mem_ip, strings[0], strings[1]);

                        if (dbMgr.result() != 1) {
                            outStream.writeObject("MsgSQL#가입된 IP주소 입니다!");
                        } else {
                            outStream.writeObject("MsgSQL#가입이 완료되었습니다.");
                        }
                        break;

                    case "Update":
                        strings = content.split("/", 2);
                        dbMgr.updateMem(mem_ip, strings[0], strings[1]);
                        if (dbMgr.result() == 1) outStream.writeObject("MsgSQL#닉네임이 변경되었습니다.");
                        break;

                    case "Delete":
                        strings = content.split("/", 2);
                        dbMgr.deleteMem(strings[0], strings[1]);
                        if (dbMgr.result() == 1) outStream.writeObject("MsgSQL#닉네임이 삭제되었습니다.");
                        break;

                    case "LoginCheck":
                        strings = content.split("/", 2);
                        int result = dbMgr.loginCheck(strings[0], Integer.parseInt(strings[1]));
                        outStream.writeObject("LoginCheck#" + result);

                        // 로그인에 성공하면 ClientList에 Outstream 추가
                        if (result == 1) sdm.clientInfoMap.put(strings[0], outStream);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        } finally {
            sdm.clientInfoMap.remove(outStream);
        }
    }
}
