package ojmChat;

import ojmDB.ProjectDAO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerThread implements Runnable {
    // 선언부 | Variable
    String mem_ip;
    String roomName;
    String nickName;
    String createMsg;
    String[] strings;
    int loginTF;

    // 선언부 | Class
    Socket clientSocket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    ServerDataMng sdm;


    // 생성자
    public ServerThread(Socket socket, ServerDataMng sdm) {
        this.clientSocket = socket;
        this.sdm = sdm;
    }


    @Override
    public void run() {
        try {
            // 입출력 스트림 객체 생성
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("I/O Stream 객체 생성 | " + clientSocket);


            // 스레드 동작 처리
            while (true) {
                String msg = (String) inStream.readObject();
                System.out.println("Thread 동작 | " + msg);

                // 프로토콜 & 컨텐츠 분리
                String[] strArray = msg.split("#", 2);
                String command = strArray[0];
                String content = strArray[1];


                // 프로토콜에 따른 서버 동작 실행
                switch(command) {
                    case "MsgSend":     /// 메세지 발송
                        sdm.saveMsg(content, mem_ip, roomName);
                        sdm.broadcastMsg("MsgSend#", roomName);
                        break;

                    case "Create":      /// 그룹창 생성
                        createMsg = ">>[" + content + "]에 입장하였습니다.";

                        // 그룹 생성 | 중복이 아니면 1, 중복이면 0 반환
                        if (sdm.createRoom(mem_ip, nickName, content, createMsg) == 0) {
                            outStream.writeObject("MsgGroup#동일한 그룹이 이미 존재합니다.");
                        } else {
                            sdm.broadcastRoomList("RoomList#");
                        }
                        break;

                    case "Enter":       /// 그룹창 입장
                        strings = content.split("/", 2);  // 그룹명과 회원명으로 분리
                        roomName = strings[0];                       // 현재 입장중인 그룹명
                        sdm.enterRoom(roomName, strings[1]);         // 그룹에 입장
                        sdm.broadcastMsg("MsgSend#", roomName);
                        break;

                    case "Join":
                        strings = content.split("/", 2);
                        mem_ip = clientSocket.getInetAddress().getHostAddress();
                        if (sdm.crudSQL("insert", mem_ip, strings[0], strings[1]) == 0) {
                            outStream.writeObject("MsgSQL#가입된 IP주소 입니다!");
                        } else {
                            outStream.writeObject("MsgSQL#가입이 완료되었습니다.");
                        }
                        break;

                    case "Update":
                        strings = content.split("/", 2);
                        if (sdm.crudSQL("update", mem_ip, strings[0], strings[1]) == 1) {
                            outStream.writeObject("MsgSQL#닉네임이 변경되었습니다.");
                        }
                        break;

                    case "Delete":
                        strings = content.split("/", 2);
                        if (sdm.crudSQL("delete", mem_ip, strings[0], strings[1]) == 1) {
                            outStream.writeObject("MsgSQL#닉네임이 삭제되었습니다.");
                        }
                        break;

                    case "LoginCheck":
                        strings = content.split("/", 2);
                        nickName = strings[0];
                        mem_ip = sdm.getIP(strings[0]);

                        loginTF = sdm.loginCheck(nickName, strings[1], outStream);
                        if(loginTF == 1) {sdm.broadcastRoomList("RoomList#");}
                        outStream.writeObject("LoginCheck#" + loginTF);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("입출력 오류 발생 | " + e.getMessage());
        } finally {
            try {
                sdm.clientInfoMap.remove(outStream);
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("커넥션 종료 중 오류 발생 | " + e.getMessage());
            }
        }
    }
}
