package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;
    Scanner sc;

    public MyClientSocket() {

        try {
            // IP주소, 포트번호
            socket = new Socket("localhost", 1077);

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            sc = new Scanner(System.in);
            System.out.println("내용을 입력하세요.");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);

            // 메인스레드 바쁘기 전에 넣어야한다.
            new Thread(() -> {
                while (true) {
                    try {
                        String inputData = reader.readLine();
                        System.out.println("받은 메세지: " + inputData);
                        if (inputData.equals("안녕")) {
                            System.out.println("대화끝");
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            //

            while (true) {
                String inputData = sc.nextLine();
                writer.write(inputData + "\n");
                writer.flush();
                if (inputData.equals("빠빠")) {
                    System.out.println("대화가 종료되었습니다.");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}

// import java.io.BufferedWriter;
// import java.io.OutputStreamWriter;
// import java.net.Socket;
// import java.util.Scanner;

// public class MyClientSocket {

// Socket socket;
// BufferedWriter writer;
// Scanner sc;

// public MyClientSocket() {

// try {

// socket = new Socket("localhost", 1077);

// writer = new BufferedWriter(
// new OutputStreamWriter(socket.getOutputStream()));
// sc = new Scanner(System.in);
// System.out.println("전송할 내용을 입력하세요.");
// while (true) {
// String inputData = sc.nextLine();
// writer.write(inputData + "/n"); // 윈도우는 기본적으로 메세지의 끝에는 \n이 필요하다. 버퍼에 담긴다.
// writer.flush(); // 버퍼에 담기고 flush해서 서버에게 보낸다.
// if (inputData.equals("")) {
// System.out.println("대화가 종료되었습니다.");
// break;
// }

// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// public static void main(String[] args) {
// new MyClientSocket();
// }
// }
