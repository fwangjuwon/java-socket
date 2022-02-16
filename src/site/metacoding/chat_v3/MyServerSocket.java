package site.metacoding.chat_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

//jwp = 주원프로토콜
//1. 최초메세지는 username으로 체킹
//2. 구분자:
//3. ALL:메세지
//4. CHAT:ID:메세지

public class MyServerSocket {

    // listener (연결받기) - main thread
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;

    // server: 메세지 받아서 보내기 (client 수마다 new thread)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new Vector<>(); // vector = 동기화가 처리된 arraylist
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

        String username;

        // 소켓을 보관할 곳
        Socket socket;

        // 내부에 buffer를 가져야한다. (고객마다)
        BufferedReader reader;
        BufferedWriter writer;
        boolean isLogin;

        public 고객전담스레드(Socket socket) {
            this.socket = socket;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ALL:뭐시라라
        public void chatPublic(String msg) {

            try {
                for (고객전담스레드 t : 고객리스트) { // 왼쪽: 컬렉션 타입, 오른쪽: 컬렉션
                    if (t != this) {
                        t.writer.write(username + ": " + msg + "\n");
                        t.writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // CHAT:주원:안녕ㅎㅎ
        public void chatPrivate(String receiver, String msg) {
            System.out.println("귓속말");
            try {
                for (고객전담스레드 t : 고객리스트) { // 왼쪽: 컬렉션 타입, 오른쪽: 컬렉션,
                    if (t.username.equals(receiver)) {
                        t.writer.write("[귓속말]" + username + ": " + msg + "\n");
                        t.writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 주원프로토콜 검사기
        // ALL:안녕
        // CHAT:주원:안녕
        public void jwp(String inputData) {
            // 1. protocol 분리
            String[] token = inputData.split(":"); // 0번지: 프로토콜, 1번지: 메세지일수도 아이디일수도,,, 프로토콜 이름으로분리
            String protocol = token[0];
            if (protocol.equals("ALL")) {
                String msg = token[1];
                chatPublic(msg);

            } else if (protocol.equals("CHAT")) {
                String receiver = token[1];
                String msg = token[2];
                chatPrivate(receiver, msg);
            } else { // 프로토콜 통과 못한것.
                System.out.println("프로토콜 없음");
            }
        }

        @Override
        public void run() {

            // 최초메세지는 username이다.
            try {
                username = reader.readLine();
                isLogin = true;
            } catch (Exception e) {
                isLogin = false;
                System.out.println("username을 받지 못했다.");
            }

            while (isLogin) {
                try {
                    String inputData = reader.readLine();
                    System.out.println("from client: " + inputData);

                    // 메세지 받았으니까 LIST<고객전담스레드> 고객리스트 <-여기에 담긴다
                    // 모든 클라이언트에게 메세지 전송(FOR문 돌려서!)
                    // 기존for문: 자유도가 높다. // for-each문: collection의 크기만큼만 돈다.
                    jwp(inputData);

                } catch (Exception e) {
                    try {
                        System.out.println("오류내용: " + e.getMessage());
                        isLogin = false;
                        고객리스트.remove(this);
                        reader.close();
                        writer.close();
                        socket.close();
                    } catch (Exception e1) {
                        System.out.println("연결해제프로세스 실패: " + e1.getMessage());
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
