package site.metacoding.chat_v2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServerSocket {

    // listener (연결받기) - main thread
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;

    // server: 메세지 받아서 보내기 (client 수마다 new thread)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new ArrayList<>();
            while (true) {
                // while돌리기 연결 계속 받아야하니까
                Socket socket = serverSocket.accept(); // main스레드가 하고 있다. 지역변수라서 계쏙 사라진다. 이 소켓을 담을 collection이 필요하다.
                                                       // 고객전담스레드 생성자에 담자. 전역변수로 list에 담아놓자.

                System.out.println("클라이언트 연결됨");

                // 한명 접속될 때마다 고객전담스레드한테 넘길거.
                고객전담스레드 t = new 고객전담스레드(socket);
                고객리스트.add(t); // 전역변수로 list에 담아놓자. 소켓이랑 t가 아직 살아 있다. t를 리스트에 담아놓으면 스택이 종료되어도 garbage
                              // collection 안생긴다.

                System.out.println("고객리스트 크기: " + 고객리스트.size());
                new Thread(t).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 내부class
    class 고객전담스레드 implements Runnable {

        // 내부에 buffer를 가져야한다. (고객마다)

        // 소켓을 보관할 곳
        Socket socket;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
