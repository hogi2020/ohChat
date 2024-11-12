package ojmChat;

import ojmDB.ProjectDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread implements Runnable {
    // 선언부
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    ServerDataMng sdm;
    ProjectDAO dbMgr;

    // 선언부 | 클라이언트 정보 선언
    String mem_ip;
    String mem_nick;
    String mem_pw;


    // 생성자 | 서버 소켓
    public ServerThread() {}
    public ServerThread(Socket socket, ServerDataMng sdm, ProjectDAO dbMgr) {
        this.clientSocket = socket;
        this.sdm = sdm;
        this.dbMgr = dbMgr;
    }


    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("입출력 Stream 객체 생성 | " + clientSocket);

            // RoomMap에 클라이언트 추가
            sdm.ClientToRoom(outStream, "NULL");
            sdm.broadcastRoomList();

            // 스레드 동작 처리
            while (true) {
                String msg = (String) inStream.readObject();
                System.out.println("스레드 동작 | " + msg);

                // 프로토콜 & 컨텐츠 분리
                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];

                // 클라이언트 ip & pw 정보 전처리
                mem_ip = clientSocket.getInetAddress().getHostAddress();
                String[] strings;

                // 선언부
                String roomName;
                ServerRoomMsg roomMsg;

                if (strArray.length == 2) {
                    // 프로토콜에 따른 서버 동작 실행
                    switch(command) {
                        case "MsgSend":     /// 메세지 발송
                            roomName = sdm.getRoomName(outStream);
                            roomMsg = sdm.getRoomMsg(roomName);
                            roomMsg.msgSave(content);
                            roomMsg.broadcastMsg(roomName);
                            // outStream.writeObject("MsgSend#" + content);
                            break;

                        case "Create":      /// 그룹창 생성
                            sdm.createRoom(content);

                            // 그룹 생성 및 중복 검사
                            if (dbMgr.insertGroup(content) == 0) {
                                outStream.writeObject("MsgGroup#동일한 그룹이 이미 존재합니다.");
                            } else {
                                ConcurrentHashMap<String, String> memMap = dbMgr.getMemMap();

                            }
                            break;

                        case "Enter":       /// 그룹창 입장
                            strings = content.split("/", 2);

                            sdm.ClientToRoom(outStream, strings[1]);
                            roomMsg = sdm.getRoomMsg(strings[1]);
                            roomMsg.addClient(outStream, strings[1]);

                            roomName = sdm.getRoomName(outStream);
                            roomMsg.broadcastMsg(roomName);

                            // 그룹 입장 선언
                            dbMgr.joinGroup(strings[0], strings[1]);
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
                            if (dbMgr.result() == 1) { outStream.writeObject("MsgSQL#닉네임이 변경되었습니다.");}
                            break;

                        case "Delete":
                            strings = content.split("/", 2);
                            dbMgr.deleteMem(strings[0], strings[1]);
                            if (dbMgr.result() == 1) { outStream.writeObject("MsgSQL#닉네임이 삭제되었습니다.");}
                            break;

                        case "LoginCheck":
                            strings = content.split("/", 2);
                            int result = dbMgr.loginCheck(strings[0], Integer.parseInt(strings[1]));
                            outStream.writeObject("LoginCheck#" + result);
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        }
    }
}
