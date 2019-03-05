import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class TCPServer implements Runnable {
    private ServerSocket server;
    private Gson json = new Gson();

    TCPServer(String host, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(host);
        server = new ServerSocket(port, 0, addr);
        System.out.format("Running on %s:%s...\n", host, port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = server.accept();

                while (true) {
                    if (client.isClosed()) {
                        break;
                    }

                    InputStreamReader in = new InputStreamReader(client.getInputStream());
                    System.out.println(new BufferedReader(in).lines().collect(Collectors.joining("\n")));
                    /*VisionTarget[] targets = json.fromJson(in, VisionTarget[].class);

                    for (VisionTarget target : targets) {
                        System.out.println(target);
                    }*/

                    in.close();
                }

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
