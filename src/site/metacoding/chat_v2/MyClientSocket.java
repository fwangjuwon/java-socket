package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;

    // 스레드 1개
    Scanner sc;
    BufferedWriter writer;
    // 스레드 2개
    BufferedReader reader;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 2000);

            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // new thread(읽기 전용)
            new Thread(new 읽기전담스레드()).start();

            // main thread (쓰기 전용) - 항상 제일 뒤에 있어야한다. 읽기부터 하고 !
            while (true) {
                String keyboardInputData = sc.nextLine();
                writer.write(keyboardInputData + "\n");
                writer.flush(); // 버퍼에 담긴 것을 스트링으로 흘려보낸다. -> 통신의 시작
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class 읽기전담스레드 implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    String inputData = reader.readLine();
                    System.out.println("받은 메세지: " + inputData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();

    }
}
