import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPServer implements Runnable {
    private DatagramSocket server;
    private Gson json = new Gson();
    private byte[] buffer = new byte[1024];

    UDPServer(String host, int port) throws IOException {
        InetAddress addr = InetAddress.getByName(host);
        server = new DatagramSocket(port, addr);
    }

    @Override
    public void run() {
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                server.receive(packet);

                VisionTarget[] targets = json.fromJson(new String(packet.getData(), 0, packet.getLength()), VisionTarget[].class);

                Main.targets.clear();
                Main.targets.addAll(Arrays.asList(targets));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
