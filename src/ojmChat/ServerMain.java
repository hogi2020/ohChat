package ojmChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    // 선언부
    Socket clientSocket;
    ServerDataMng sdm;
    DBManager dbManager;


    // 서버 실행 및 클라이언트 접속
    public void ServerStart() {
        // DBManager 초기화 및 연결
        dbManager = new DBManager();
        dbManager.connect();

        try(ServerSocket ss = new ServerSocket(3000)) {
            System.out.println("Ready to Server..... | " + ss);

            // 채팅데이터 관리를 위한 Map 객체 생성
            sdm = new ServerDataMng();

            while(true) {
                // 새로운 클라이언트가 들어올때까지 accept()는 Block!!
                clientSocket = ss.accept();
                System.out.println("클라이언트 접속 | " + clientSocket.getInetAddress());

                // ServerThread 클래스의 run()스레드 생성
                new Thread(new ServerThread(clientSocket, sdm)).start();
            }
        } catch (IOException e) {
            System.out.println("서버 작동 중 오류 발생: " + e.getMessage());
        } finally {
            dbManager.disconnect(); // 서버 종료 시 DB 연결 종료
        }
    }


    // 메인 메소드 실행
    public static void main(String[] args) {
        new ServerMain().ServerStart();
    }
}