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
    String nickName;
    String[] strings;
    int loginTF;


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
                        sdm.broadcastMsg(content, mem_ip, roomName);
                        break;

                    case "Create":      /// 그룹창 생성
                        // 그룹 생성 및 중복 검사
                        if (dbMgr.insertGroup(content) == 0) {
                            outStream.writeObject("MsgGroup#동일한 그룹이 이미 존재합니다.");
                        } else {
                            // Map을 통해 각 그룹에 대한 메세지 리스트 생성
                            sdm.roomMsgMap.put(content, new CopyOnWriteArrayList<>());
                            dbMgr.joinGroup(nickName, content);
                            dbMgr.insertMsg(">>["+ content +"]에 입장하였습니다.", mem_ip, content);
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
                        mem_ip = clientSocket.getInetAddress().getHostAddress();
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
                        loginTF = dbMgr.loginCheck(strings[0], Integer.parseInt(strings[1]));
                        nickName = strings[0];
                        mem_ip = dbMgr.getIP(strings[0]);
                        outStream.writeObject("LoginCheck#" + loginTF);

                        // 로그인에 성공하면 ClientList에 Outstream 추가
                        if (loginTF == 1) {
                            sdm.clientInfoMap.put(strings[0], outStream);
                            sdm.broadcastRoomList();
                        }
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
