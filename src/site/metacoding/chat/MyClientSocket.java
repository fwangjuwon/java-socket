package site.metacoding.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077);
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            writer.write("안녕\n"); // 윈도우는 기본적으로 메세지의 끝에는 \n이 필요하다. 버퍼에 담긴다.
            writer.flush(); // 버퍼에 담기고 flush해서 서버에게 보낸다.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
