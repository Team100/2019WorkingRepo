import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 8080;

        if (System.getenv("type").equals("tcp")) {
            try {
                TCPServer s = new TCPServer(host, port);
                Thread t = new Thread(s);
                t.start();
                t.join();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                UDPServer s = new UDPServer(host, port);
                Thread t = new Thread(s);
                t.start();
                t.join();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
