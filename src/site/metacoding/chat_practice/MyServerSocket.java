package site.metacoding.chat_practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServerSocket {

    ServerSocket serverSocket;
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    Scanner sc;

    public MyServerSocket() {

        try {
            serverSocket = new ServerSocket(1088);
            System.out.println("서버소켓 생성");
            socket = serverSocket.accept();

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            System.out.println("클라이언트 연결");

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            sc = new Scanner(System.in);
            System.out.println("메세지 입력: ");

            new Thread(() -> {
                while (true) {
                    try {
                        String inputData = sc.nextLine();
                        writer.write(inputData + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true) {
                String inputData = reader.readLine();
                System.out.println("받은 메세지: " + inputData);
                if (inputData.equals("이제그만")) {
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("main 끝");
    }
}
