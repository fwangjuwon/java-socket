package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    ServerSocket serverSocket;
    Socket socket;
    BufferedReader reader;

    // 클라이언트한테 메세지 보내기)
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {

        try {
            serverSocket = new ServerSocket(1077);
            System.out.println("서버 소켓 생성됨");
            socket = serverSocket.accept();// while을 돌면서 대기 (내부적)

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            System.out.println("클라이언트 연결");

            // main thread바쁘니까 while전에 buffered writer 해야해
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            sc = new Scanner(System.in);
            System.out.println("내용을 입력하세요.");

            new Thread(() -> {
                while (true) {
                    try {
                        String inputData = sc.nextLine();
                        writer.write(inputData + "\n");
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // main thread가 한다 = 메세지 반복해서 받는 서버 소켓
            while (true) {
                String inputData = reader.readLine();
                System.out.println("받은 메시지 : " + inputData);
                if (inputData.equals("")) {
                    System.out.println("대화가 종료되었습니다.");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("통신 오류 발생 : " + e.getMessage());
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("main 종료");
    }

}

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.ServerSocket;
// import java.net.Socket;

// public class MyServerSocket {

// ServerSocket serverSocket; // 리스너 (연결되기를 기다리는 포트->세션이 만들어짐)
// // java가 들고 있는 class -> 시스템콜: 시스템이 들고 있는 라이브러리를 땡겨와서 쓰는 것

// Socket socket; // 메시지 통신(해당포트와 통신하는 포트)
// // -> 포트가 하나만 있을 수 없다.
// BufferedReader reader;

// public MyServerSocket() {
// try {
// // 1. 서버 소켓 생성(ㄹㅣ스너)
// serverSocket = new ServerSocket(1077); // 내부적으로 while이 돈다(계속 기다려야하니까)
// 타임슬립걸려있다.
// // wellknown port는 쓰면 안돼!! (0~1023)
// System.out.println("서버 소켓생성");
// socket = serverSocket.accept(); // while을 돌면서 대기 클라이언트가 접속할 떄까지(random port)
// -> demon!!
// // 실행 -> 여기까지 대기타고 있는다. -> myclientsocket실행 -> 접속됨 -> 다음라인 실행

// reader = new BufferedReader(
// new InputStreamReader(socket.getInputStream()));

// while (true) {
// String inputData = reader.readLine();
// System.out.println("받은 메세지: " + inputData);
// System.out.println("클라이언트 연결됨");

// }
// } catch (Exception e) {
// System.out.println("통신오류 발생: " + e.getMessage());
// // e.printStackTrace();
// }
// }

// public static void main(String[] args) {
// new MyServerSocket();
// System.out.println("main종료");
// }
// }
