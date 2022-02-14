package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    ServerSocket serverSocket; // 리스너 (연결되기를 기다리는 포트->세션이 만들어짐)
    // java가 들고 있는 class -> 시스템콜: 시스템이 들고 있는 라이브러리를 땡겨와서 쓰는 것

    Socket socket; // 메시지 통신(해당포트와 통신하는 포트)
    // -> 포트가 하나만 있을 수 없다.
    BufferedReader reader;

    public MyServerSocket() {
        try {
            // 1. 서버 소켓 생성(ㄹㅣ스너)
            serverSocket = new ServerSocket(1077); // 내부적으로 while이 돈다(계속 기다려야하니까) 타임슬립걸려있다.
            // wellknown port는 쓰면 안돼!! (0~1023)
            System.out.println("서버 소켓생성");
            socket = serverSocket.accept(); // while을 돌면서 대기 클라이언트가 접속할 떄까지(random port) -> demon!!
            // 실행 -> 여기까지 대기타고 있는다. -> myclientsocket실행 -> 접속됨 -> 다음라인 실행

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String inputData = reader.readLine();
            System.out.println("받은 메세지: " + inputData);
            System.out.println("클라이언트 연결됨");

        } catch (Exception e) {
            System.out.println("통신오류 발생: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("main종료");
    }
}
