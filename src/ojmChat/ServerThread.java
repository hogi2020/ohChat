package ojmChat;

import ojmDB.DBManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    // 선언부
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    ServerDataMng sdm;
    DBManager dbMgr;

    // 선언부 | 클라이언트 정보 선언
    String mem_ip;
    String mem_nick;
    String mem_pw;


    // 생성자 | 서버 소켓
    public ServerThread() {}
    public ServerThread(Socket socket, ServerDataMng sdm, DBManager dbMgr) {
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
                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];

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
                            break;
                        case "Enter":
                            sdm.ClientToRoom(outStream, content);
                            roomMsg = sdm.getRoomMsg(content);
                            roomMsg.addClient(outStream, content);

                            roomName = sdm.getRoomName(outStream);
                            roomMsg.broadcastMsg(roomName);
                            break;
                        case "Join":
                            mem_ip = clientSocket.getInetAddress().getHostAddress();
                            String[] insertStrings = content.split("/", 2);
                            dbMgr.insertMem(mem_ip, insertStrings[0], insertStrings[1]);

                            if (dbMgr.result() != 1) {
                                outStream.writeObject("MsgSQL#가입된 IP주소 입니다!");
                            } else {
                                outStream.writeObject("MsgSQL#가입이 완료되었습니다.");
                            }
                            break;
                        case "Update":
                            mem_ip = clientSocket.getInetAddress().getHostAddress();
                            String[] updateStrings = content.split("/", 2);
                            dbMgr.updateMem(mem_ip, updateStrings[0], updateStrings[1]);

                            if (dbMgr.result() == 1) {
                                outStream.writeObject("MsgSQL#닉네임이 변경되었습니다.");
                            }
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        }
    }
}
