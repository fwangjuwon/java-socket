package site.metacoding.chat_practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.sql.rowset.spi.SyncResolver;

public class MyClientSocket {

    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    Scanner sc;

    public MyClientSocket() {

        try {
            socket = new Socket("localhost", 1088);

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            sc = new Scanner(System.in);
            System.out.println("메세지를 입력하세요: ");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sc = new Scanner(System.in);

            new Thread(() -> {
                while (true) {
                    try {
                        String inputData = reader.readLine();
                        System.out.println("받은메세지: " + inputData);

                        if (inputData.equals("이제안녕")) {
                            break;
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();

            while (true) {
                String inputData = sc.nextLine();
                writer.write(inputData + "\n");
                writer.flush();
                if (inputData.equals("이제그만")) {
                    break;
                }
            }
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
